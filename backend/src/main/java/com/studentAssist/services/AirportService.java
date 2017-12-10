package com.studentAssist.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentAssist.dao.AirportDAO;
import com.studentAssist.entities.Airport;

@Service
public class AirportService {

	@Autowired
	AirportDAO airportDAO;

	public List<Airport> getAirportServices(int universityID) {
		return airportDAO.getAirportServices(universityID);
	}
}
