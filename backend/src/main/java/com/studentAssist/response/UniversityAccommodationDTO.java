package com.studentAssist.response;

import java.util.List;

public class UniversityAccommodationDTO {

	private long universityId;

	private List<RAccommodationAdd> accommodationAdds;

	private String universityName;

	private String description;

	private String urls;

	private int noOfUsers;

	private String location;

	private int estdYear;

	public long getUniversityId() {
		return universityId;
	}

	public void setUniversityId(long universityId) {
		this.universityId = universityId;
	}

	public List<RAccommodationAdd> getAccommodationAdds() {
		return accommodationAdds;
	}

	public void setAccommodationAdds(List<RAccommodationAdd> accommodationAdds) {
		this.accommodationAdds = accommodationAdds;
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

}
