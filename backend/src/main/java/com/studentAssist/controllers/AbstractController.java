package com.studentAssist.controllers;

import com.studentAssist.entities.Users;
import com.studentAssist.util.SAConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AbstractController {

	protected Users getUserFromRequest(HttpServletRequest request) {
		return (Users) request.getAttribute(SAConstants.ACCESS_TOKEN);
	}

}
