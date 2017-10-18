package com.studentAssist.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ApartmentName")
public class RApartmentNames
{
  String apartmentName;
  
  public RApartmentNames(String apartmentName)
  {
    this.apartmentName = apartmentName;
  }
  
  public String getApartmentName()
  {
    return this.apartmentName;
  }
  
  @XmlElement
  public void setApartmentName(String apartmentName)
  {
    this.apartmentName = apartmentName;
  }
  
  public RApartmentNames() {}
}
