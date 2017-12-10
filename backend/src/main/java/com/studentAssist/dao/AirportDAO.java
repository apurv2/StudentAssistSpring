
package com.studentAssist.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.studentAssist.entities.AccommodationAdd;
import com.studentAssist.entities.Airport;

@Repository
@Transactional
public class AirportDAO extends AbstractDao {

	public List<Airport> getAirportServices(int universityID) {
		List services;
		services = null;

		Query query = getSession().createQuery("from Airport where UNIVERSITY_ID =" + universityID);
		services = query.list();
		lazyLoadUniversityPhotos(services);
		return services;
	}

	private void lazyLoadUniversityPhotos(List<Airport> services) {

		for (Airport airportService : services) {
			Hibernate.initialize(airportService.getUniversity().getUniversityPhotos());
		}
	}
}
