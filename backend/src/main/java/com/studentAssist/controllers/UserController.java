package com.studentAssist.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.studentAssist.entities.Users;
import com.studentAssist.services.UserService;
import com.studentAssist.util.CreateUserDetails;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

	@Autowired
	private UserService userService;

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT, value = "/createUser")
	public String createUser(@RequestBody CreateUserDetails createUserDetails, HttpServletRequest request)
			throws Exception {

		Users user = getUserFromRequest(request);
		if (createUserDetails != null) {
			return userService.createUser(user, createUserDetails.getRegistrationId(),
					createUserDetails.getInstanceId(), createUserDetails.getSelectedUniversityIds());
		} else {
			return userService.createUser(user, null, null, null);
		}
	}

}
