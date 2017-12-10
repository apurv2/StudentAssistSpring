package com.studentAssist.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.studentAssist.dao.AbstractDao;
import com.studentAssist.entities.Airport;
import com.studentAssist.entities.Universities;
import com.studentAssist.services.AirportService;
import com.studentAssist.util.Utilities;

@RestController

public class AirportController extends AbstractDao {

	@Autowired
	private AirportService airportService;

	@RequestMapping(method = RequestMethod.POST, value = "/createAirportService")
	public void createService(HttpServletRequest request) {
		Airport airport = new Airport();
		Airport airport2 = new Airport();

		airport.setAddedDate(Utilities.getDate());
		airport.setServiceName("FSI (Fine Arts Society of India)");
		airport.setGroupLink("http://www.uta.edu/studentorgs/fsi/frontend/#new-student");
		airport.setDescription("Free Airport pickup and temporary accommodation available");
		airport.setUniversity(new Universities(1));

		airport2.setAddedDate(Utilities.getDate());
		airport2.setServiceName("BIG HOWDY");
		airport2.setGroupLink("http://utabighowdy.isisites.net/airport-pickup-form/");
		airport2.setDescription("Free Airport Pickup now available");
		airport2.setUniversity(new Universities(1));

		persist(airport2);
		persist(airport);

	}

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/getAirportServiceList/{universityId}")
	public List<Airport> getAllServices(int universityId) {

		return airportService.getAirportServices(universityId);
	}
}
