package com.studentAssist.exceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.studentAssist.entities.Users;
import com.studentAssist.interceptor.ExecuteInterceptor;
import com.studentAssist.services.LoggingService;
import com.studentAssist.util.FBGraph;
import com.studentAssist.util.SAConstants;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

	private static final Logger logger = Logger.getLogger(ExecuteInterceptor.class);

	@Autowired
	LoggingService loggingService;

	@Autowired
	FBGraph fbGraph;

	@ExceptionHandler(value = IOException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	ErrorInfo handleIOException(Exception e, HttpServletRequest request, HttpServletResponse response) {

		List<String> parameterList = new ArrayList();
		Iterator it = request.getParameterMap().entrySet().iterator();
		try {

			while (it.hasNext()) {
				String parameter = "";

				Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();

				String key = entry.getKey();
				String[] value = entry.getValue();

				parameter += key + "=";

				if (value != null && value.length > 0) {
					parameter += value[0];
				}

				parameterList.add(parameter);
			}

			logger.info("access denied 401");

			loggingService.logMessage(ExceptionUtils.getStackTrace(e), request.getHeader(SAConstants.ACCESS_TOKEN),
					parameterList);
			return new ErrorInfo(SAConstants.FAILURE, SAConstants.RESPONSE_EXCEPTION);

		} catch (Exception ex) {
			logger.info("Exception raised inside GloblExceptionHandler" + ExceptionUtils.getStackTrace(ex));
			return new ErrorInfo(SAConstants.FAILURE, SAConstants.RESPONSE_EXCEPTION);
		} finally {
			logger.info(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public ErrorInfo handleException(Exception e, HttpServletRequest request) {

		List<String> parameterList = new ArrayList();
		Iterator it = request.getParameterMap().entrySet().iterator();

		try {

			while (it.hasNext()) {
				String parameter = "";

				Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();

				String key = entry.getKey();
				String[] value = entry.getValue();

				parameter += key + "=";

				if (value != null && value.length > 0) {
					parameter += value[0];
				}

				parameterList.add(parameter);
			}

			String access_token = request.getHeader(SAConstants.ACCESS_TOKEN);
			Users user = fbGraph.getUserDetails(access_token);

			String userId = "";
			if (user != null) {
				userId = user.getUserId() + "";
			} else {
				userId = request.getHeader(SAConstants.ACCESS_TOKEN);
			}

			loggingService.logMessage(ExceptionUtils.getStackTrace(e), userId, parameterList);

			return new ErrorInfo(SAConstants.FAILURE, SAConstants.RESPONSE_EXCEPTION);
		} catch (Exception ex) {
			logger.info("Exception raised inside GloblExceptionHandler" + ExceptionUtils.getStackTrace(ex));
			return new ErrorInfo(SAConstants.FAILURE, SAConstants.RESPONSE_EXCEPTION);
		} finally {
			logger.info(ExceptionUtils.getStackTrace(e));
		}
	}

}
