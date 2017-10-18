package com.studentAssist.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.studentAssist.response.RNotificationSettings;
import com.studentAssist.services.NotificationsService;

@RestController
@RequestMapping("/notifications")
public class NotificationsController extends AbstractController {

	@Autowired
	private NotificationsService notificationService;

	@RequestMapping(method = RequestMethod.GET, value = "/getNotificationSettings")
	public RNotificationSettings getNotificationSettings(HttpServletRequest request) throws Exception {

		return notificationService.getNotificationSettings(getUserFromRequest(request));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/subscribeNotifications")
	public String subscribeNotifications(@RequestBody RNotificationSettings notifications, HttpServletRequest request)
			throws Exception {

		return notificationService.subscribeNotifications(getUserFromRequest(request), notifications,
				notifications.getGcmId(), notifications.getInstanceId());

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/unSubscribeNotifications")
	public String unSubscribeNotifications(String accommodationBOdy, HttpServletRequest request) throws Exception {

		return notificationService.unSubscribeNotifications(getUserFromRequest(request));

	}

}
