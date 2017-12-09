package com.studentAssist.response;

import java.util.List;

public class AccommodationSearchDTO {

	private String leftSpinner;

	private String rightSpinner;

	private List<Long> universityIds;

	private String apartmentName;

	private String apartmentType;

	private String gender;

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLeftSpinner() {
		return leftSpinner;
	}

	public void setLeftSpinner(String leftSpinner) {
		this.leftSpinner = leftSpinner;
	}

	public String getRightSpinner() {
		return rightSpinner;
	}

	public void setRightSpinner(String rightSpinner) {
		this.rightSpinner = rightSpinner;
	}

	public List<Long> getUniversityIds() {
		return universityIds;
	}

	public void setUniversityIds(List<Long> universityIds) {
		this.universityIds = universityIds;
	}

}
