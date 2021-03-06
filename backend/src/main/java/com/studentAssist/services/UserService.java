package com.studentAssist.services;

import com.studentAssist.dao.UserDAO;
import com.studentAssist.entities.GCMIds;
import com.studentAssist.entities.Universities;
import com.studentAssist.entities.Users;
import com.studentAssist.exception.BadStudentRequestException;
import com.studentAssist.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

	@Autowired
	UserDAO userDAO;

	public String createUser(Users user, String gcmId, String deviceId, List<Integer> univIds) throws Exception {

		List<Universities> universities = new ArrayList<>();

		// truncating array list to accept only 4 universities
		if (univIds != null && univIds.size() > 4) {
			univIds = univIds.subList(0, 4);
		}

		user.setRegisteredDate(Utilities.getDate());
		GCMIds id;
		System.out.println("gcmId=" + gcmId + "deviceId=" + deviceId);

		if (gcmId == null || deviceId == null) {
			id = null;
		} else {
			id = new GCMIds(gcmId, deviceId, Utilities.getDate());
		}
		if (univIds != null && !univIds.isEmpty()) {
			for (int univId : univIds) {
				Universities university = new Universities();
				university.setUniversityId(univId);
				universities.add(university);
			}
			user.setUniversities(universities);
		}
		return userDAO.createUser(user, id);
	}

	public boolean checkAdminUserId(long userId) {

		List<Users> users = userDAO.checkAdminUserId(userId);
		return (users != null && !users.isEmpty());

	}
	
	public void validateAdminUser(long userId) throws BadStudentRequestException {

		if (!checkAdminUserId(userId)) {
			throw new BadStudentRequestException();
		}

	}

}
