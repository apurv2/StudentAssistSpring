package com.studentAssist.controllers;


import com.studentAssist.exceptionHandler.ErrorInfo;
import com.studentAssist.services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.studentAssist.util.SAConstants.EXCEPTION_TYPE_CLIENT;

@RestController
public class LogResource extends AbstractController {

    @Autowired
    LoggingService loggingService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "errors")
    public void logError(@RequestBody ErrorInfo errorInfo,
                         HttpServletRequest request) throws Exception {

        String userId = "";
        if (getUserFromRequest(request) != null) {
            userId = String.valueOf(getUserFromRequest(request).getUserId());
        }
        loggingService.logMessage(errorInfo.getMessage(), userId, request, EXCEPTION_TYPE_CLIENT);
    }
}