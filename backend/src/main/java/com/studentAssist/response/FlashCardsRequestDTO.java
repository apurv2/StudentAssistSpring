package com.studentAssist.response;

import java.util.List;

public class FlashCardsRequestDTO {
	List<Integer> universityIDs;
	int currentUniversityID;

	public List<Integer> getUniversityIDs() {
		return universityIDs;
	}

	public void setUniversityIDs(List<Integer> universityIDs) {
		this.universityIDs = universityIDs;
	}

	public int getCurrentUniversityID() {
		return currentUniversityID;
	}

	public void setCurrentUniversityID(int currentUniversityID) {
		this.currentUniversityID = currentUniversityID;
	}

}
