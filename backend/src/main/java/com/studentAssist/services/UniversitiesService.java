package com.studentAssist.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentAssist.dao.UniversitiesDAO;
import com.studentAssist.entities.Universities;
import com.studentAssist.entities.Users;
import com.studentAssist.response.RUniversity;
import com.studentAssist.util.SAConstants;

@Service
public class UniversitiesService {

	@Autowired
	UniversitiesDAO universitiesDAO;

	public List<RUniversity> getUniversityNames(Users currentUser) throws Exception {

		List<Object[]> universities = universitiesDAO.getUniversityNames(currentUser);
		return getRuniversity(universities);
	}

	private List<RUniversity> getRuniversity(List<Object[]> universityObjects) {

		List<RUniversity> rUniv = new ArrayList<RUniversity>();

		for (Object[] universityObj : universityObjects) {
			rUniv.add(new RUniversity((int) universityObj[0], (String) universityObj[8], (String) universityObj[4],
					(String) universityObj[11], ((BigInteger) universityObj[12]).intValue(),
					(String) universityObj[2] + ", " + (String) universityObj[6],
					((Integer) universityObj[5]).intValue(), ((BigInteger) universityObj[10]).intValue(),
					(String) universityObj[7]));
		}
		return rUniv;
	}

	public String doesUserHaveUnivs(Users currentUser) {

		return universitiesDAO.doesUserHaveUnivs(currentUser) ? SAConstants.USER_HAS_UNIVS
				: SAConstants.USER_DOES_NOT_HAVE_UNIVS;

	}

	public List<RUniversity> getUserUniversities(Users currentUser) throws Exception {

		List<RUniversity> allUniversities = getRuniversity(universitiesDAO.getUniversityNames(currentUser));

		Users dbUser = universitiesDAO.getUser(currentUser);

		if (dbUser != null) {
			populateUserSelectedUniversities(allUniversities, universitiesDAO.getUserUniversities(currentUser));
		}
		return allUniversities;

	}

	private void populateUserSelectedUniversities(List<RUniversity> allUniversities,
			List<Universities> userUniversities) {

		for (RUniversity dbUniversity : allUniversities) {
			for (Universities userUniversity : userUniversities) {
				if (userUniversity.getUniversityId() == dbUniversity.getUniversityId()) {
					dbUniversity.setSelected(true);

				}

			}

		}

	}

	public List<String> getAllUniversityNames() {

		return universitiesDAO.getAllUniversityNames();
	}

	public List<RUniversity> getUniversityNamesWithId(Users user) {

		List<RUniversity> rUniv = new ArrayList<RUniversity>();

		List<Object[]> universities = universitiesDAO.getUniversityNamesWithId(user);

		for (Object[] universitiy : universities) {
			rUniv.add(new RUniversity((Integer) universitiy[0], (String) universitiy[1]));
		}
		return rUniv;

	}

	public List<RUniversity> getUniversitiesByName(String searchString) {

		List<Universities> dbUnivs = universitiesDAO.getUniversitiesByName(searchString);
		List<RUniversity> universities = new ArrayList();

		RUniversity univDTO;
		for (Universities univ : dbUnivs) {

			univDTO = new RUniversity();
			univDTO.setUniversityId(univ.getUniversityId());
			univDTO.setUniversityName(univ.getUniversityName());
			univDTO.setUnivAcronym(univ.getUnivAcronym());

			universities.add(univDTO);
		}

		return universities;
	}

}
