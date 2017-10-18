package com.studentAssist.response;

import java.util.List;

public class RAccommodationAddJson {
	
	private int universityId;
	private List<RAccommodationAdd> accommodationAdd;
	
	public RAccommodationAddJson() {}
	
	public RAccommodationAddJson(int universityId, List<RAccommodationAdd> accommodationAdd) {
		this.universityId = universityId;
		this.accommodationAdd = accommodationAdd;
	}
	
	public int getUniversityId() {
		return universityId;
	}
	public void setUniversityId(int universityId) {
		this.universityId = universityId;
	}
	public List<RAccommodationAdd> getAccommodationAdd() {
		return accommodationAdd;
	}
	public void setAccommodationAdd(List<RAccommodationAdd> accommodationAdd) {
		this.accommodationAdd = accommodationAdd;
	}

}
