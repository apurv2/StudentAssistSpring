package com.studentAssist.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentAssist.classes.ApartmentDetails;
import com.studentAssist.dao.AccommodationDAO;
import com.studentAssist.dao.NotificationsDAO;
import com.studentAssist.entities.Apartments;
import com.studentAssist.entities.GCMIds;
import com.studentAssist.entities.NotificationSettings;
import com.studentAssist.entities.Universities;
import com.studentAssist.entities.Users;
import com.studentAssist.response.RApartmentNamesInUnivs;
import com.studentAssist.response.RApartmentNamesWithType;
import com.studentAssist.response.RNotificationSettings;
import com.studentAssist.util.Utilities;

@Service
public class NotificationsService {

	@Autowired
	NotificationsDAO notificationsDAO;

	@Autowired
	AccommodationDAO accommodationDAO;

	public RNotificationSettings getNotificationSettings(Users user) throws Exception {

		RNotificationSettings notificationSettings = null;

		List<NotificationSettings> notifications = notificationsDAO.getNotificationSettings(user.getUserId());

		List<RApartmentNamesInUnivs> allApartmentNames = getApartmentNamesWithTypeAndUniv(user);

		if (notifications != null && notifications.size() > 0) {

			for (NotificationSettings notificationSetting : notifications) {

				notificationSettings = new RNotificationSettings(notificationSetting.getApartmentName(),
						notificationSetting.getGender(), notificationSetting.getUniversityId().getUniversityId(),
						notificationSetting.getApartmentType(), allApartmentNames);

			}
		} else {
			return new RNotificationSettings(null, null, -1, null, allApartmentNames);
		}

		return notificationSettings;
	}

	public String subscribeNotifications(Users user, RNotificationSettings notificationSettings, String gcmId,
			String deviceId) throws Exception {

		GCMIds gcmIds;
		if (gcmId == null || deviceId == null) {
			gcmIds = null;
		} else {
			gcmIds = new GCMIds(gcmId, deviceId, Utilities.getDate());
		}

		Universities university = new Universities();
		university.setUniversityId(notificationSettings.getUniversityId());

		return notificationsDAO
				.subscribeNotifications(user,
						new NotificationSettings(null, notificationSettings.getApartmentName(),
								notificationSettings.getGender(), notificationSettings.getApartmentType(), university),
						gcmIds);
	}

	public String unSubscribeNotifications(Users user) throws Exception {

		return notificationsDAO.unSubscribeNotifications(user);
	}

	public List<RApartmentNamesInUnivs> getApartmentNamesWithTypeAndUniv(Users user) {

		List<Apartments> apartments = accommodationDAO.getApartmentNamesWithTypeAndUniv(user);

		List<ApartmentDetails> apartmentDetails = new ArrayList<>();
		List<RApartmentNamesInUnivs> apartmentNamesInUnivs = new ArrayList<>();

		List<RApartmentNamesWithType> apartmentNames = new ArrayList<>();

		int count = 0;
		int apartmentsSize = apartments.size();

		for (Apartments apartment : apartments) {

			// making this list to keep things simple. No real use of this
			apartmentDetails.add(new ApartmentDetails(apartment.getUniversity().getUniversityId(),
					apartment.getUniversity().getUniversityName()));

			if ((count > 0 && apartmentDetails.get(count - 1).getUniverstiyId() != apartmentDetails.get(count)
					.getUniverstiyId()) || count == apartmentsSize - 1) {

				int position = 0;
				if (count == apartmentsSize - 1) {
					position = count;
					apartmentNames.add(
							new RApartmentNamesWithType(apartment.getApartmentName(), apartment.getApartmentType()));
				} else {
					position = count - 1;
				}

				apartmentNamesInUnivs.add(new RApartmentNamesInUnivs(apartmentDetails.get(position).getUniversityName(),
						apartmentDetails.get(position).getUniverstiyId(), apartmentNames));

				apartmentNames = new ArrayList<>();

			}
			apartmentNames.add(new RApartmentNamesWithType(apartment.getApartmentName(), apartment.getApartmentType()));
			count++;
		}

		return apartmentNamesInUnivs;
	}

}
