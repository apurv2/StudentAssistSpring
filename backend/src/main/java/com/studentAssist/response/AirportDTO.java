package com.studentAssist.response;

public class AirportDTO {
	private int universityId;

	private String universityName;

	private String univAcronym;

	private String universityPhotoUrl;

	private String universityPickupUrl;

	public int getUniversityId() {
		return universityId;
	}

	public void setUniversityId(int universityId) {
		this.universityId = universityId;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	public String getUnivAcronym() {
		return univAcronym;
	}

	public void setUnivAcronym(String univAcronym) {
		this.univAcronym = univAcronym;
	}

	public String getUniversityPhotoUrl() {
		return universityPhotoUrl;
	}

	public void setUniversityPhotoUrl(String universityPhotoUrl) {
		this.universityPhotoUrl = universityPhotoUrl;
	}

	public String getUniversityPickupUrl() {
		return universityPickupUrl;
	}

	public void setUniversityPickupUrl(String universityPickupUrl) {
		this.universityPickupUrl = universityPickupUrl;
	}

}
