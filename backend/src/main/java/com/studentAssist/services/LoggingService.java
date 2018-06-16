package com.studentAssist.services;

import com.studentAssist.dao.LoggingDAO;
import com.studentAssist.entities.Log;
import com.studentAssist.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoggingService {

    @Autowired
    LoggingDAO loggingDao;

    public void logMessage(String logDetail, String userIdOrAccessToken, HttpServletRequest request, int exceptionType) {

        if (logDetail != null && logDetail.length() > 4000) {
            logDetail = logDetail.substring(0, 4000);
        }

        Log log = new Log(logDetail, Utilities.getDate(), userIdOrAccessToken, exceptionType, getParameterList(request));
        loggingDao.insertException(log);
    }

    private List<String> getParameterList(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .map(item -> item.getKey() + "=" + item.getValue())
                .collect(Collectors.toList());

    }
}
