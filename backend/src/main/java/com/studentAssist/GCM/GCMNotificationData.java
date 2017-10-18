package com.studentAssist.GCM;

public class GCMNotificationData
{
  private String parameter1;
  private String parameter2;
  
  public GCMNotificationData() {}
  
  public String getParameter1()
  {
    return this.parameter1;
  }
  
  public void setParameter1(String parameter1)
  {
    this.parameter1 = parameter1;
  }
  
  public String getParameter2()
  {
    return this.parameter2;
  }
  
  public void setParameter2(String parameter2)
  {
    this.parameter2 = parameter2;
  }
  
  public GCMNotificationData(String parameter1, String parameter2)
  {
    this.parameter1 = parameter1;
    this.parameter2 = parameter2;
  }
}
