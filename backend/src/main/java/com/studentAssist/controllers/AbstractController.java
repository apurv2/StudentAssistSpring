package com.studentAssist.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studentAssist.entities.Users;
import com.studentAssist.exception.InvalidTokenException;
import com.studentAssist.util.FBGraph;
import com.studentAssist.util.SAConstants;

@Component
public class AbstractController {

	protected Users getUserFromRequest(HttpServletRequest request) {

		return (Users) request.getAttribute(SAConstants.ACCESS_TOKEN);

	}

}
