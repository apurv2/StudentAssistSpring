package com.studentAssist.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.studentAssist.entities.Airport;
import com.studentAssist.services.AirportService;

@RestController
@RequestMapping("/airport")

public class AirportController {

	@Autowired
	private AirportService airportService;

	// @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method =
	// RequestMethod.PUT, value = "/createAccommodationAddFromFacebook")
	// public void createService() {
	// Airport airport = new Airport();
	// Airport airport2 = new Airport();
	//
	//
	//
	// airport.setAddedDate(Utilities.getDate());
	// airport.setServiceName("FSI (Fine Arts Society of India)");
	// airport.setGroupLink("http://www.uta.edu/studentorgs/fsi/frontend/#new-student");
	// airport.setDescription("Free Airport pickup and temporary accommodation
	// available");
	//
	// airport2.setAddedDate(Utilities.getDate());
	// airport2.setServiceName("BIG HOWDY");
	// airport2.setGroupLink("http://utabighowdy.isisites.net/airport-pickup-form/");
	// airport2.setDescription("Free Airport Pickup now available");
	//
	// session.beginTransaction();
	//
	// session.save(airport);
	// session.save(airport2);
	//
	// session.getTransaction().commit();
	// }

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/getAirportServiceList")
	public List<Airport> getAllServices() {

		return airportService.getAirportServices();
	}
}
