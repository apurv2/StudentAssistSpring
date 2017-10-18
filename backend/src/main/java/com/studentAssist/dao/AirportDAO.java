
package com.studentAssist.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.studentAssist.entities.Airport;

@Repository
@Transactional
public class AirportDAO extends AbstractDao {

	public List<Airport> getAirportServices() {
		List services;
		services = null;

		Query query = getSession().createQuery("from Airport");
		services = query.list();
		return services;
	}

}
