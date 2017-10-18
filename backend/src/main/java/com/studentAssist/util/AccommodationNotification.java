package com.studentAssist.util;


import java.util.Date;

import com.studentAssist.entities.Users;

public class AccommodationNotification
{
  private Users user;
  int notificationId;
  Date createDate;
  
  public Users getUser()
  {
    return this.user;
  }
  
  public Date getCreateDate()
  {
    return this.createDate;
  }
  
  public void setCreateDate(Date createDate)
  {
    this.createDate = createDate;
  }
  
  public void setUser(Users user)
  {
    this.user = user;
  }
  
  public int getNotificationId()
  {
    return this.notificationId;
  }
  
  public void setNotificationId(int notificationId)
  {
    this.notificationId = notificationId;
  }
  
  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (this.user == null ? 0 : this.user.hashCode());
    return result;
  }
}
