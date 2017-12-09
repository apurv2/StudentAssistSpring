package com.studentAssist.response;

import java.util.List;

public class FlashCardsRequestDTO {
	int numOfAccomodataionCards;
	int numOfAirportCards;
	List<Integer> universitiesIDs;
	int currentUniversityID;

	public int getNumOfAccomodataionCards() {
		return numOfAccomodataionCards;
	}

	public void setNumOfAccomodataionCards(int numOfAccomodataionCards) {
		this.numOfAccomodataionCards = numOfAccomodataionCards;
	}

	public int getNumOfAirportCards() {
		return numOfAirportCards;
	}

	public void setNumOfAirportCards(int numOfAirportCards) {
		this.numOfAirportCards = numOfAirportCards;
	}

	public List<Integer> getUniversitiesIDs() {
		return universitiesIDs;
	}

	public void setUniversitiesIDs(List<Integer> universitiesIDs) {
		this.universitiesIDs = universitiesIDs;
	}

	public int getCurrentUniversityID() {
		return currentUniversityID;
	}

	public void setCurrentUniversityID(int currentUniversityID) {
		this.currentUniversityID = currentUniversityID;
	}

}
