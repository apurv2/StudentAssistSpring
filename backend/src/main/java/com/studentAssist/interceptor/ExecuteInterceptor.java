package com.studentAssist.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.studentAssist.entities.Users;
import com.studentAssist.exception.StudenAssitException;
import com.studentAssist.util.FBGraph;
import com.studentAssist.util.SAConstants;

@Configuration
public class ExecuteInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(ExecuteInterceptor.class);

	@Autowired
	FBGraph fbGraph;

	// before the actual handler will be executed
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.info("inside pre handle");

		String access_token = request.getHeader(SAConstants.ACCESS_TOKEN);

		if (access_token == null) {
			System.out.println("no token");
			setUnauthorizedHeader(response);
			return false;
		}

		Users user = fbGraph.getUserDetails(access_token);

		if (user == null) {
			setUnauthorizedHeader(response);
			return false;
		}

		request.setAttribute(SAConstants.ACCESS_TOKEN, user);

		return true;
	}

	private void setUnauthorizedHeader(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponseWrapper.SC_UNAUTHORIZED);
	}

}