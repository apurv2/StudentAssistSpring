package com.studentAssist.response;

import java.util.List;

public class FlashCardsResponseDTO {
	List<RAccommodationAdd> accomodationCards;
	AirportDTO airportCard;
	int currentUniversityID;

	public List<RAccommodationAdd> getAccomodationCards() {
		return accomodationCards;
	}

	public void setAccomodationCards(List<RAccommodationAdd> accomodationCards) {
		this.accomodationCards = accomodationCards;
	}

	public AirportDTO getAirportCard() {
		return airportCard;
	}

	public void setAirportCard(AirportDTO airportCard) {
		this.airportCard = airportCard;
	}

	public int getCurrentUniversityID() {
		return currentUniversityID;
	}

	public void setCurrentUniversityID(int currentUniversityID) {
		this.currentUniversityID = currentUniversityID;
	}
}
