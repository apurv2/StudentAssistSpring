package com.studentAssist.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentAssist.dao.LoggingDAO;
import com.studentAssist.entities.Log;
import com.studentAssist.util.Utilities;

@Service
public class LoggingService {

	@Autowired
	LoggingDAO loggingDao;

	public void logMessage(String logDetail, String userId, List parameterList) {

		if (logDetail != null && logDetail.length() > 4900) {
			logDetail = logDetail.substring(0, 4000);
		}
		Log log = new Log(logDetail, Utilities.getDate(), userId, parameterList);
		loggingDao.insertException(log);
	}
}
