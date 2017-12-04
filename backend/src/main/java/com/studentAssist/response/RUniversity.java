package com.studentAssist.response;

public class RUniversity {

	private int universityId;

	private String universityName;

	private String description;

	private String urls;

	private int noOfUsers;

	private String location;

	private int estdYear;

	private int noOfListings;

	private String univAcronym;

	private boolean isSelected;

	public RUniversity(int universitiyId, String universityName) {

		this.universityId = universitiyId;
		this.universityName = universityName;
	}

	public RUniversity() {
	}

	public RUniversity(int universityId, String universityName, String description, String urls, int noOfUsers,
			String location, int estdYear, int noOfListings, String univAcronym) {
		this.universityId = universityId;
		this.universityName = universityName;
		this.description = description;
		this.urls = urls;
		this.noOfUsers = noOfUsers;
		this.location = location;
		this.estdYear = estdYear;
		this.noOfListings = noOfListings;
		this.univAcronym = univAcronym;
	}

	public int getNoOfListings() {
		return noOfListings;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void setNoOfListings(int noOfListings) {
		this.noOfListings = noOfListings;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

	public int getNoOfUsers() {
		return noOfUsers;
	}

	public void setNoOfUsers(int noOfUsers) {
		this.noOfUsers = noOfUsers;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getEstdYear() {
		return estdYear;
	}

	public void setEstdYear(int estdYear) {
		this.estdYear = estdYear;
	}

	public String getUnivAcronym() {
		return univAcronym;
	}

	public void setUnivAcronym(String univAcronym) {
		this.univAcronym = univAcronym;
	}

}
