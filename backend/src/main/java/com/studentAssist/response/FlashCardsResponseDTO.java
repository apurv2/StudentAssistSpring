package com.studentAssist.response;

import java.util.List;

public class FlashCardsResponseDTO {
	List<RAccommodationAdd> accomodationCards;
	List<AirportDTO> airportCards;
	public List<AirportDTO> getAirportCards() {
		return airportCards;
	}

	public void setAirportCards(List<AirportDTO> airportCards) {
		this.airportCards = airportCards;
	}

	int currentUniversityID;

	public List<RAccommodationAdd> getAccomodationCards() {
		return accomodationCards;
	}

	public void setAccomodationCards(List<RAccommodationAdd> accomodationCards) {
		this.accomodationCards = accomodationCards;
	}

	

	public int getCurrentUniversityID() {
		return currentUniversityID;
	}

	public void setCurrentUniversityID(int currentUniversityID) {
		this.currentUniversityID = currentUniversityID;
	}
}
