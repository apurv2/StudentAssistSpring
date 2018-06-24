
package com.studentAssist.dao;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.studentAssist.GCM.PushNotification;
import com.studentAssist.entities.AccommodationAdd;
import com.studentAssist.entities.Apartments;
import com.studentAssist.entities.GCMIds;
import com.studentAssist.entities.NotificationSettings;
import com.studentAssist.entities.UserAccommodationNotifications;
import com.studentAssist.entities.Users;
import com.studentAssist.response.RAccommodationAdd;
import com.studentAssist.util.SAConstants;
import com.studentAssist.util.Utilities;

@Repository
@Transactional
public class NotificationsDAO extends AbstractDao {

	/**
	 * fetches Notifications settings
	 *
	 * @param userId
	 * @param session
	 * @return
	 */
	public List<NotificationSettings> getNotificationSettings(long userId) throws Exception {
		List<NotificationSettings> settings = null;

		Users user = getByKey(Users.class, userId);
		user.getNotificationSettings();
		if (user != null) {

			for (NotificationSettings setting : user.getNotificationSettings()) {
				Hibernate.initialize(setting.getApartmentName());
				Hibernate.initialize(setting.getApartmentType());
			}

		}

		if (user != null) {
			settings = user.getNotificationSettings();
		} else {
			return null;
		}

		return settings;

	}

	/**
	 *
	 * @param userId
	 * @param notificationSettings
	 * @param session
	 * @param gcmId
	 *            -- adding GCM Id here also to the user just to ensure to save
	 *            updated GcdId of the device. Manually checking for save or
	 *            update here because we are putting one to many relation with
	 *            Users -> NotificationSettings table due to constrains with
	 *            hibernate.
	 * @return
	 */
	public String subscribeNotifications(Users user, NotificationSettings notificationSettings, GCMIds gcmId)
			throws Exception {

		if (gcmId != null) {
			// adding gcm Ids to User and vice versa
			this.addGcmIdToUser(user, gcmId);
			saveOrUpdate(gcmId);
			// session.saveOrUpdate((Object) gcmId);
		}

		this.addNotificationToUser(user, notificationSettings);
		notificationSettings.setCreateDate(Utilities.getDate());

		saveOrUpdate(notificationSettings);
		return SAConstants.RESPONSE_SUCCESS;

	}

	/**
	 * Fetches the list of users to send notification and sends them.
	 *
	 * @param session
	 * @param apartment
	 * @param advertisement
	 * @param user
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public void sendNotification(Apartments apartment, AccommodationAdd advertisement, Users user)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {

		// session.beginTransaction();
		// session.clear();
		HashSet<GCMIds> notificationSet = new HashSet<GCMIds>();

		// Retrieving the criterion parameters
		String apartmentType = apartment.getApartmentType();
		String apartmentName = apartment.getApartmentName();
		String gender = advertisement.getGender();

		String sql = "select  {NotificationSettings.*} from NotificationSettings  where exists"
				+ " (SELECT null from NotificationSettings_apartmentType "
				+ " WHERE apartmentType =:apartmentType) and gender=:gender ";

		SQLQuery query = getSession().createSQLQuery(sql);
		query.addEntity("NotificationSettings", NotificationSettings.class);
		query.setParameter("apartmentType", apartmentType);
		query.setParameter("gender", gender);
		List<NotificationSettings> notifications = query.list();
		UserAccommodationNotifications notification;

		for (NotificationSettings notificationSetting : notifications) {

			List<String> apartMentNameList = notificationSetting.getApartmentName();

			if (apartMentNameList.isEmpty() || apartMentNameList.contains(apartmentName)) {

				// userIds are selected for notification
				Users notificationElegibleUser = notificationSetting.getUser();

				// saving the user's notification to the database so that he
				// can retrieve their list of notifications later also
				notification = new UserAccommodationNotifications();
				notification.setCreateDate(Utilities.getDate());
				linkNotificationToUser(notificationElegibleUser, notification);
				linkNotificationToAccommodationAdd(advertisement, notification);

				persist(notification);
				// getSession().save(notification);

				List<GCMIds> gcmIds = notificationElegibleUser.getGcmIds();
				notificationSet.addAll(gcmIds);

			}
		}
		if (!notificationSet.isEmpty()) {
			PushNotification pushNotification = new PushNotification();

			pushNotification.processData(new RAccommodationAdd(advertisement.getVacancies(), advertisement.getGender(),
					advertisement.getNoOfRooms(), advertisement.getCost(), advertisement.getFbId(),
					advertisement.getNotes(), user.getUserId(), apartmentName, user.getFirstName(), user.getLastName(),
					user.getEmail(), user.getPhoneNumber(), advertisement.getAddId(), false,
					new SimpleDateFormat("dd MMM").format(advertisement.getDatePosted()),
					advertisement.getAddPhotoIds(), advertisement.getApartment().getUniversity().getUniversityId(),
					advertisement.getApartment().getUniversity().getUniversityName(),
					advertisement.getApartment().getUniversity().getUnivAcronym(),
					advertisement.getApartment().getUniversity().getUniversityPhotos().get(0).getPhotoUrl(),
					advertisement.getApartment().getId(), advertisement.getPostedTill()), notificationSet);
		}

	}

	public String unSubscribeNotifications(Users user) throws Exception {

		NotificationSettings settings = getByKey(NotificationSettings.class, user.getUserId());

		if (settings != null) {
			delete(settings);
		}
		return SAConstants.RESPONSE_SUCCESS;
	}

	public void addGcmIdToUser(Users user, GCMIds gcmId) {

		user.getGcmIds().add(gcmId);
		gcmId.setUser(user);

	}

	private void linkNotificationToUser(Users user, UserAccommodationNotifications notification) {

		user.getUserNotifications().add(notification);
		notification.setUser(user);

	}

	private void addNotificationToUser(Users user, NotificationSettings notification) {

		user.getNotificationSettings().add(notification);
		notification.setUser(user);
		notification.setCheckerUserId(user.getUserId());

	}

	private void linkNotificationToAccommodationAdd(AccommodationAdd advertisement,
			UserAccommodationNotifications notification) {
		advertisement.getUserNotifications().add(notification);
		notification.setAccommodationAdd(advertisement);

	}

}
