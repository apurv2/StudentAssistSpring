package com.studentAssist.exceptionHandler;

import com.studentAssist.entities.Users;
import com.studentAssist.exception.BadStudentRequestException;
import com.studentAssist.exception.InvalidTokenException;
import com.studentAssist.interceptor.ExecuteInterceptor;
import com.studentAssist.services.LoggingService;
import com.studentAssist.util.FBGraph;
import com.studentAssist.util.SAConstants;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.studentAssist.util.SAConstants.EXCEPTION_TYPE_BAD_REQUEST;
import static com.studentAssist.util.SAConstants.EXCEPTION_TYPE_GENERIC;
import static com.studentAssist.util.SAConstants.EXCEPTION_TYPE_UNAUTHORIZED;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(ExecuteInterceptor.class);

    @Autowired
    LoggingService loggingService;

    @Autowired
    FBGraph fbGraph;

    @ExceptionHandler(value = InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    ErrorInfo handleInvalidTokenException(Exception exception, HttpServletRequest request,
                                          HttpServletResponse response) {

        try {
            logger.info("access denied 401");
            loggingService.logMessage(ExceptionUtils.getStackTrace(exception)
                    , request.getHeader(SAConstants.ACCESS_TOKEN), request, EXCEPTION_TYPE_UNAUTHORIZED);
            return new ErrorInfo(SAConstants.FAILURE, SAConstants.RESPONSE_EXCEPTION);

        } catch (Exception ex) {
            logger.info("Exception raised inside GloblExceptionHandler" + ExceptionUtils.getStackTrace(ex));
            return new ErrorInfo(SAConstants.FAILURE, SAConstants.RESPONSE_EXCEPTION);
        } finally {
            logger.info(ExceptionUtils.getStackTrace(exception));
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ErrorInfo handleException(Exception exception, HttpServletRequest request) {

        try {
            String accessToken = request.getHeader(SAConstants.ACCESS_TOKEN);

            Users user = fbGraph.getUserDetails(accessToken);
            String userId = user != null ? String.valueOf(user.getUserId()) : accessToken;
            loggingService.logMessage(ExceptionUtils.getStackTrace(exception), userId, request, EXCEPTION_TYPE_GENERIC);


            return new ErrorInfo(SAConstants.FAILURE, SAConstants.RESPONSE_EXCEPTION);
        } catch (Exception ex) {
            logger.info("Exception raised inside GloblExceptionHandler" + ExceptionUtils.getStackTrace(ex));
            return new ErrorInfo(SAConstants.FAILURE, SAConstants.RESPONSE_EXCEPTION);
        } finally {
            logger.info(ExceptionUtils.getStackTrace(exception));
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadStudentRequestException.class)
    public ErrorInfo handleBadStudentException(Exception exception, HttpServletRequest request) {

        try {
            String accessToken = request.getHeader(SAConstants.ACCESS_TOKEN);
            Users user = fbGraph.getUserDetails(accessToken);
            String userId = user != null ? String.valueOf(user.getUserId()) : accessToken;
            loggingService.logMessage(ExceptionUtils.getStackTrace(exception), userId, request,EXCEPTION_TYPE_BAD_REQUEST );

            String exceptionMessage = exception.getMessage().isEmpty()
                    ? SAConstants.RESPONSE_EXCEPTION : exception.getMessage();
            return new ErrorInfo(SAConstants.FAILURE, exceptionMessage);
        } catch (Exception ex) {
            return new ErrorInfo(SAConstants.FAILURE, SAConstants.RESPONSE_EXCEPTION);
        } finally {
            logger.info(ExceptionUtils.getStackTrace(exception));
        }
    }
}
