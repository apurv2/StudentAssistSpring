package com.studentAssist.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ApartmentName")
public class RApartmentNamesWithType {

	String apartmentName;
	String apartmentType;
	int universityId;

	public int getUinversityId() {
		return universityId;
	}

	public void setUinversityId(int uinversityId) {
		this.universityId = uinversityId;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public String getApartmentType() {
		return apartmentType;
	}

	public void setApartmentType(String apartmentType) {
		this.apartmentType = apartmentType;
	}

	public RApartmentNamesWithType(String apartmentName, String apartmentType) {
		super();
		this.apartmentName = apartmentName;
		this.apartmentType = apartmentType;
	}

	public RApartmentNamesWithType(String apartmentName, String apartmentType, int uinversityId) {
		super();
		this.apartmentName = apartmentName;
		this.apartmentType = apartmentType;
		this.universityId = uinversityId;
	}

	public RApartmentNamesWithType(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public RApartmentNamesWithType() {

	}

}
