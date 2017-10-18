package com.studentAssist.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Notification")
public class RAccommodationNotification
{
  String notificationId;
  String spinner1;
  String spinner2;
  int notificationType;
  
  public RAccommodationNotification() {}
  
  public String getNotificationId()
  {
    return this.notificationId;
  }
  
  @XmlElement
  public void setNotificationId(String notificationId)
  {
    this.notificationId = notificationId;
  }
  
  public RAccommodationNotification(String notificationId, String spinner1, String spinner2, int notificationType)
  {
    this.notificationId = notificationId;
    this.spinner1 = spinner1;
    this.spinner2 = spinner2;
    this.notificationType = notificationType;
  }
  
  public String getSpinner1()
  {
    return this.spinner1;
  }
  
  @XmlElement
  public void setSpinner1(String spinner1)
  {
    this.spinner1 = spinner1;
  }
  
  public String getSpinner2()
  {
    return this.spinner2;
  }
  
  @XmlElement
  public void setSpinner2(String spinner2)
  {
    this.spinner2 = spinner2;
  }
  
  public int getNotificationType()
  {
    return this.notificationType;
  }
  
  @XmlElement
  public void setNotificationType(int notificationType)
  {
    this.notificationType = notificationType;
  }
}
