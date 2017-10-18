package com.studentAssist.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name="AirportServices")
public class Airport
{
  @Id
  @GeneratedValue
  @Column(name="ID")
  int id;
  Date addedDate;
  String serviceName;
  String description;
  String groupLink;
  
  public String getGroupLink()
  {
    return this.groupLink;
  }
  
  @XmlElement
  public void setGroupLink(String groupLink)
  {
    this.groupLink = groupLink;
  }
  
  public Date getAddedDate()
  {
    return this.addedDate;
  }
  
  @XmlElement
  public void setAddedDate(Date addedDate)
  {
    this.addedDate = addedDate;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  @XmlElement
  public void setId(int id)
  {
    this.id = id;
  }
  
  public String getServiceName()
  {
    return this.serviceName;
  }
  
  @XmlElement
  public void setServiceName(String serviceName)
  {
    this.serviceName = serviceName;
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  @XmlElement
  public void setDescription(String description)
  {
    this.description = description;
  }
}
