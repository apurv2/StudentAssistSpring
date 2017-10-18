package com.studentAssist.GCM;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.studentAssist.entities.GCMIds;
import com.studentAssist.response.RAccommodationAdd;

public class PushNotification {

	// sends push notification
	public void processData(RAccommodationAdd advertisement, Set<GCMIds> notificationSet)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		Class objClass = advertisement.getClass();
		List<GCMNotificationData> notificationData = new ArrayList();

		Method[] methods = objClass.getMethods();
		Method[] arrayOfMethod1;
		int j = (arrayOfMethod1 = methods).length;
		for (int i = 0; i < j; i++) {
			Method method = arrayOfMethod1[i];
			String name = method.getName();
			if (name.startsWith("get")) {
				name = name.substring(3, name.length());
				char[] c = name.toCharArray();
				c[0] = Character.toLowerCase(c[0]);
				name = new String(c);

				System.out.println("name==" + name + " value==" + method.invoke(advertisement, null));
				String value = String.valueOf(method.invoke(advertisement, null));

				if (value == null || value.equals("null")) {
					notificationData.add(new GCMNotificationData(name, ""));
				} else {
					notificationData.add(new GCMNotificationData(name, value));

				}

			}
		}
		notificationData.add(new GCMNotificationData("notificationType", "accommodationAdd"));
		createBuilder(notificationSet, notificationData);
	}

	private void sendMessage(Message.Builder builder, List<String> gcmIds) throws IOException {
		String API_KEY = "AIzaSyAX2IklMMg2_zQDGWTSn37su_JH5-LWduQ";

		Sender sender = new Sender(API_KEY);

		Message message = builder.build();
		MulticastResult result = sender.send(message, gcmIds, 1);

		System.out.println("result = " + result);
		if (result.getResults() == null) {
			int error = result.getFailure();
			System.out.println("Broadcast failure: " + error);
		}
	}

	private void createBuilder(Set<GCMIds> notificationSet, List<GCMNotificationData> notificationData)
			throws IOException {
		Message.Builder builder = new Message.Builder();

		String collpaseKey = "gcm_message";
		builder.collapseKey(collpaseKey);
		builder.timeToLive(30);
		builder.delayWhileIdle(true);
		for (GCMNotificationData data : notificationData) {
			builder.addData(data.getParameter1(), data.getParameter2());
		}

		List<String> gcmIds = new ArrayList();
		for (GCMIds gcmId : notificationSet) {
			gcmIds.add(gcmId.getGcmId());
		}
		sendMessage(builder, gcmIds);
	}
}
