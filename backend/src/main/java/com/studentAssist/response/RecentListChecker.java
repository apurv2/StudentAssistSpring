package com.studentAssist.response;

public class RecentListChecker
{
  String addId;
  
  public String getAddId()
  {
    return this.addId;
  }
  
  public void setAddId(String addId)
  {
    this.addId = addId;
  }
  
  public RecentListChecker(String addId)
  {
    this.addId = addId;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    RecentListChecker that = (RecentListChecker)o;
    
    return getAddId().equals(that.getAddId());
  }
  
  public int hashCode()
  {
    return getAddId().hashCode();
  }
}
