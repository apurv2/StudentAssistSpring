package com.studentAssist.services;

import com.studentAssist.dao.AccommodationDAO;
import com.studentAssist.dao.NotificationsDAO;
import com.studentAssist.entities.*;
import com.studentAssist.exception.BadStudentRequestException;
import com.studentAssist.response.RApartmentNamesInUnivs;
import com.studentAssist.response.RApartmentNamesWithType;
import com.studentAssist.response.RNotificationSettings;
import com.studentAssist.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationsService {

    @Autowired
    NotificationsDAO notificationsDAO;

    @Autowired
    AccommodationDAO accommodationDAO;

    public RNotificationSettings getNotificationSettings(Users user) throws Exception {

        RNotificationSettings notificationSettings = null;
        List<NotificationSettings> notifications = notificationsDAO.getNotificationSettings(user.getUserId());
        int selectedUnivId = notifications.get(0).getUniversityId().getUniversityId();
        List<RApartmentNamesInUnivs> allApartmentNames = getApartmentNamesWithTypeAndUniv(user, selectedUnivId);

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

        validateNotificationSettings(notificationSettings);

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

    public List<RApartmentNamesInUnivs> getApartmentNamesWithTypeAndUniv(Users user, int univId) {

        List<Apartments> apartments = accommodationDAO.getApartmentNamesWithTypeAndUniv(user, univId);
        Map<Integer, List<RApartmentNamesWithType>> univApts = new HashMap<>();
        Map<Integer, String> univIdNameMap = new HashMap<>();

        apartments.forEach(apartment -> {
            int universityId = apartment.getUniversity().getUniversityId();
            String universityName = apartment.getUniversity().getUniversityName();
            univIdNameMap.put(universityId, universityName);

            if (!univApts.containsKey(universityId)) {
                univApts.put(universityId, new ArrayList<>());
            } else {
                univApts.get(universityId)
                        .add(new RApartmentNamesWithType(apartment.getApartmentName(), apartment.getApartmentType()));
            }
        });

        return univApts.entrySet().stream().
                map(item -> new RApartmentNamesInUnivs() {{
                    setApartmentNames(item.getValue());
                    setUniversityId(item.getKey());
                    setUniversityName(univIdNameMap.get(item.getKey()));
                }}).collect(Collectors.toList());
    }

    private void validateNotificationSettings(RNotificationSettings notificationSettings) throws BadStudentRequestException {
        if (notificationSettings != null && notificationSettings.getApartmentType().isEmpty()) {
            throw new BadStudentRequestException();
        }

    }
}
