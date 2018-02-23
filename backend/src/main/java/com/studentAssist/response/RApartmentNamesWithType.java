package com.studentAssist.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ApartmentName")
public class RApartmentNamesWithType {

	String apartmentName;
	String apartmentType;
	int universityId;
	int apartmentId;

	public int getUniversityId() {
		return universityId;
	}

	public void setUniversityId(int universityId) {
		this.universityId = universityId;
	}

	public int getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(int apartmentId) {
		this.apartmentId = apartmentId;
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

	public RApartmentNamesWithType(String apartmentName, String apartmentType, int uinversityId, int apartmentId) {
		super();
		this.apartmentName = apartmentName;
		this.apartmentType = apartmentType;
		this.universityId = uinversityId;
		this.apartmentId = apartmentId;

	}

	public RApartmentNamesWithType(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public RApartmentNamesWithType() {

	}

}
