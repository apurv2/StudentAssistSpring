
package com.studentAssist.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.studentAssist.entities.GCMIds;
import com.studentAssist.entities.UserDummy;
import com.studentAssist.entities.Users;
import com.studentAssist.util.SAConstants;

@Repository
@Transactional
public class UserDAO extends AbstractDao {

	@Autowired
	NotificationsDAO notificationsDAO;

	/**
	 * Creates and updates user information
	 * 
	 * @param user
	 *            - contains user object information
	 * @param session
	 *            - session variable for hibernate
	 * @param id
	 *            - GCM Ids -- required for adnroid every time the app is opened
	 *            because GCM Id may be refreshed by google.
	 * @return - Returns success or failure of create user operation
	 */
	public String createUser(Users user, GCMIds id) throws Exception {

		if (user.getUniversities().size() == 0) {

			// check if its a new user
			if (getByKey(Users.class, user.getUserId()) == null) {

				saveOrUpdate(user);
			} else {

				UserDummy userDummy = new UserDummy(user);
				userDummy.setId(user.getUserId());
				saveOrUpdate(userDummy);
			}

		} else {
			saveOrUpdate(user);
		}

		// adding gcm Ids to User and vice versa
		if (id != null) {
			notificationsDAO.addGcmIdToUser(user, id);
			// session.saveOrUpdate((Object) id);
			saveOrUpdate(id);
		}
		return SAConstants.RESPONSE_SUCCESS;
	}

}
