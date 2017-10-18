package com.studentAssist.GCM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.studentAssist.util.AccommodationNotification;

public class SendNotification
{
  private static void sendMessage(Set<AccommodationNotification> notificationSet, List<GCMNotificationData> notificationData)
    throws IOException
  {
    String API_KEY = "AIzaSyDGnCgvHV1vDQW-WyDFnVZOuZTZ4X_uNLA";
    String collpaseKey = "gcm_message";
    String messageStr = "message content here";
    String messageId = "fyMURqt_TPA:APA91bFLkBaDES90gLmGjjUU8CzALidPHGCTCJSrwUJM-licfbPAV0aoyB9cJngtbz5BQU1oh3EtW4u2AYXmXibciuKr5dKcoXDe7RB67qGbfoWMrFwCOvErkwnflkkymsZBQk3I_T8K";
    
    Sender sender = new Sender(API_KEY);
    Message.Builder builder = new Message.Builder();
    
    builder.collapseKey(collpaseKey);
    builder.timeToLive(30);
    builder.delayWhileIdle(true);
    builder.addData("message", messageStr);
    
    Message message = builder.build();
    
    List<String> androidTargets = new ArrayList();
    androidTargets.add(messageId);
    
    MulticastResult result = sender.send(message, androidTargets, 1);
    System.out.println("result = " + result);
    if (result.getResults() == null)
    {
      int error = result.getFailure();
      System.out.println("Broadcast failure: " + error);
    }
  }
}
