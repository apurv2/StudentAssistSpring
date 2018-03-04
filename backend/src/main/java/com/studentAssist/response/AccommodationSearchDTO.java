package com.studentAssist.response;

import java.util.List;

public class AccommodationSearchDTO {

	private String leftSpinner;

	private String rightSpinner;

	private List<Integer> universityIds;

	private String apartmentName;

	private String apartmentType;

	private String gender;

	private int selectedUniversityId;

	private int paginationPosition;

	public int getSelectedUniversityId() {
		return selectedUniversityId;
	}

	public int getPaginationPosition() {
		return paginationPosition;
	}

	public void setPaginationPosition(int paginationPosition) {
		this.paginationPosition = paginationPosition;
	}

	public void setSelectedUniversityId(int selectedUniversityId) {
		this.selectedUniversityId = selectedUniversityId;
	}

	public List<Integer> getUniversityIds() {
		return universityIds;
	}

	public void setUniversityIds(List<Integer> universityIds) {
		this.universityIds = universityIds;
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

}
