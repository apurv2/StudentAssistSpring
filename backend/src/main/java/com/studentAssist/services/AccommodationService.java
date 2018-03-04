
package com.studentAssist.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.studentAssist.dao.AccommodationDAO;
import com.studentAssist.entities.AccommodationAdd;
import com.studentAssist.entities.Apartments;
import com.studentAssist.entities.Universities;
import com.studentAssist.entities.UniversityPhotos;
import com.studentAssist.entities.UserVisitedAdds;
import com.studentAssist.entities.Users;
import com.studentAssist.response.AccommodationSearchDTO;
import com.studentAssist.response.RAccommodationAdd;
import com.studentAssist.response.RAccommodationAddJson;
import com.studentAssist.response.RApartmentNames;
import com.studentAssist.response.RApartmentNamesWithType;
import com.studentAssist.response.UniversityAccommodationDTO;
import com.studentAssist.util.SAConstants;
import com.studentAssist.util.Utilities;

@Service
public class AccommodationService {

	@Autowired
	AccommodationDAO accommmodationDAO;

	public String createAccommodationAddFromFacebook(String userId, String apartmentName, String noOfRooms,
			String vacancies, String cost, String gender, String fbId, String notes, String firstName, String lastName,
			List<String> addPhotoIds) throws Exception {

		Users user = new Users();

		user.setFirstName(firstName);
		user.setLastName(lastName);
		// user.setUserId(userId);

		user.setRegisteredDate(Utilities.getDate());

		AccommodationAdd advertisement = new AccommodationAdd(vacancies, gender, noOfRooms, cost, fbId, notes,
				Utilities.getDate(), addPhotoIds);

		return accommmodationDAO.createAccommodationAddFromFacebook(user, advertisement, apartmentName);

	}

	public List<RAccommodationAdd> getAccommodationNotifications(Users user, int position) throws Exception {

		List<AccommodationAdd> accommodationAdds;
		accommodationAdds = accommmodationDAO.getAccommodationNotifications(user, position);

		List<Long> addIds = getUserVisitedAdds(user);
		return getRAccommodationAdds(accommodationAdds, addIds, -1);
	}

	public String createAccommodationAdd(AccommodationAdd add, long userId, int apartmentId) throws Exception {

		add.setDatePosted(Utilities.getDate());
		return accommmodationDAO.createAccommodationAdd(userId, add, apartmentId);
	}

	public String deleteAccommodationAdd(int addId, Users users) throws Exception {

		AccommodationAdd add = accommmodationDAO.getByKey(AccommodationAdd.class, addId);
		long userId = add != null ? add.getUser().getUserId() : 0;

		if (userId > 0 && userId == users.getUserId() || SAConstants.ADMIN_USER_ID.containsKey(userId)) {

			accommmodationDAO.deleteAccommodationAdd(add);

			return SAConstants.RESPONSE_SUCCESS;

		}

		throw new RuntimeException();
	}

	public List<RAccommodationAdd> getUserPosts(long userId, int position) throws Exception {

		List<AccommodationAdd> userAdds = accommmodationDAO.getUserPosts(userId, position);
		return getRAccommodationAdds(userAdds, null, -1);

	}

	/**
	 * gets all Apartment Names for the User's chosen Universities. usage:
	 * simpleSearch, advancedSearch, PostAccommodation.
	 * 
	 * @param currentUser
	 * @return
	 * @throws Exception
	 */
	public List<RApartmentNames> getAllApartmentNames(Users currentUser) throws Exception {

		List<Apartments> apartments = accommmodationDAO.getAllApartmentNames(currentUser);

		List<RApartmentNames> rApartments = new ArrayList<>();

		for (Apartments apt : apartments) {
			rApartments.add(new RApartmentNames(apt.getApartmentName()));
		}
		return rApartments;
	}

	public List<RAccommodationAdd> getAdvancedAdvertisements(String apartmentName, String gender, int universityId,
			int position) throws Exception {

		List<AccommodationAdd> advancedSearchAdds = accommmodationDAO.getAdvancedAdvertisements(apartmentName, gender,
				universityId, position);

		// List<Long> addIds = getUserVisitedAdds(list);

		return getRAccommodationAdds(advancedSearchAdds, null, -1);

	}

	public List<UniversityAccommodationDTO> getSimpleSearchAdds(AccommodationSearchDTO accommodationSearch)
			throws Exception {

		List<AccommodationAdd> simpleSearchAdds = accommmodationDAO.getSimpleSearchAdds(
				accommodationSearch.getLeftSpinner(), accommodationSearch.getRightSpinner(),
				accommodationSearch.getUniversityIds());

		List<RAccommodationAdd> accomodationAdds = getRAccommodationAdds(simpleSearchAdds, null, 2);

		Map<Integer, UniversityAccommodationDTO> perUnivListing = new HashMap<Integer, UniversityAccommodationDTO>();

		for (RAccommodationAdd add : accomodationAdds) {

			if (perUnivListing.containsKey(add.getUniversityId())) {

				UniversityAccommodationDTO univAdd = perUnivListing.get(add.getUniversityId());
				univAdd.getAccommodationAdds().add(add);

			} else {
				List<RAccommodationAdd> addsList = new ArrayList<RAccommodationAdd>();
				UniversityAccommodationDTO univAcc = new UniversityAccommodationDTO();

				addsList.add(add);
				univAcc.setAccommodationAdds(addsList);
				univAcc.setUniversityId(add.getUniversityId());
				univAcc.setUniversityName(add.getUniversityName());
				univAcc.setUrls(add.getUniversityPhotoUrl());

				perUnivListing.put(add.getUniversityId(), univAcc);
			}
		}

		List<UniversityAccommodationDTO> univAddsList = new ArrayList();
		Iterator it = perUnivListing.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			UniversityAccommodationDTO dto = perUnivListing.get(pair.getKey());
			univAddsList.add(dto);
		}

		return univAddsList;

	}

	public List<RAccommodationAdd> getSimpleSearchAddsPagination(String leftSpinner, String rightSpinner, int position,
			int universityId) {

		List<AccommodationAdd> simpleSearchAdds = accommmodationDAO.getSimpleSearchAddsPagination(leftSpinner,
				rightSpinner, position, universityId);
		return getRAccommodationAdds(simpleSearchAdds, null, -1);
	}

	public List<RApartmentNames> getApartmentNames(String apartmentType) throws Exception {

		List<Apartments> apartments = accommmodationDAO.getApartmentNames(apartmentType);
		List<RApartmentNames> rApartments = new ArrayList<>();
		for (Apartments apt : apartments) {
			rApartments.add(new RApartmentNames(apt.getApartmentName()));
		}
		return rApartments;
	}

	public List<RApartmentNamesWithType> getApartmentNamesWithType(List<Integer> universityIds) throws Exception {

		List<Apartments> apartments = accommmodationDAO.getApartmentNamesWithType(universityIds);
		List<RApartmentNamesWithType> rApartments = new ArrayList<>();

		for (Apartments apartment : apartments) {
			rApartments.add(new RApartmentNamesWithType(apartment.getApartmentName(), apartment.getApartmentType(),
					apartment.getUniversity().getUniversityId(), apartment.getId()));
		}
		return rApartments;
	}

	public String setUserVisitedAdds(Users user, int addId) throws Exception {

		UserVisitedAdds userVisitedAdds = new UserVisitedAdds();
		AccommodationAdd accommodationAdd = new AccommodationAdd();
		accommodationAdd.setAddId(addId);

		userVisitedAdds.setUser(user);
		userVisitedAdds.setCreateDate(Utilities.getDate());
		userVisitedAdds.setAccommodationAdd(accommodationAdd);

		// doubt them
		user.getUserVisitedAdds().add(userVisitedAdds);
		accommodationAdd.getUserVisitedAdds().add(userVisitedAdds);

		return accommmodationDAO.setUserVisitedAdds(userVisitedAdds);
	}

	public List<Long> getUserVisitedAdds(Users user) throws Exception {

		return accommmodationDAO.getUserVisitedAdds(user);

	}

	public List<RAccommodationAdd> getRecentlyViewed(Users user, int position) {

		List<AccommodationAdd> accommodationAdds;

		accommodationAdds = accommmodationDAO.getRecentlyViewed(user, position);

		return getRAccommodationAdds(accommodationAdds, null, -1);

	}

	/**
	 * priority => 1. Logo 2. Building Image
	 * 
	 * @param accommodationAdds
	 * @param addIds
	 * @return
	 */
	public List<RAccommodationAdd> getRAccommodationAdds(List<AccommodationAdd> accommodationAdds, List<Long> addIds,
			int photoPriority) {

		List<RAccommodationAdd> rAdds = new ArrayList<RAccommodationAdd>();

		for (AccommodationAdd add : accommodationAdds) {

			String universityPhoto = SAConstants.EmptyText;
			List<UniversityPhotos> photos = add.getUniversity().getUniversityPhotos();
			if (photoPriority != -1) {
				for (UniversityPhotos photo : photos) {
					if (photo.getPhotoPriority() == photoPriority) {
						universityPhoto = photo.getPhotoUrl();
						break;
					}

				}
			}

			Users user = add.getUser();
			if (addIds != null) {

				if (addIds.contains(add.getAddId())) {
					rAdds.add(new RAccommodationAdd(add.getVacancies(), add.getGender(), add.getNoOfRooms(),
							add.getCost(), add.getFbId(), add.getNotes(), user.getUserId(),
							add.getApartment().getApartmentName(), user.getFirstName(), user.getLastName(),
							user.getEmail(), user.getPhoneNumber(), add.getAddId(), true,
							new SimpleDateFormat("dd MMM").format(add.getDatePosted()), add.getAddPhotoIds(),
							add.getApartment().getUniversity().getUniversityId(),
							add.getApartment().getUniversity().getUniversityName(), universityPhoto,
							add.getApartment().getUniversity().getUnivAcronym(), add.getApartment().getCity(),
							add.getApartment().getState(), add.getApartment().getZip()));
				} else {
					rAdds.add(new RAccommodationAdd(add.getVacancies(), add.getGender(), add.getNoOfRooms(),
							add.getCost(), add.getFbId(), add.getNotes(), user.getUserId(),
							add.getApartment().getApartmentName(), user.getFirstName(), user.getLastName(),
							user.getEmail(), user.getPhoneNumber(), add.getAddId(), false,
							new SimpleDateFormat("dd MMM").format(add.getDatePosted()), add.getAddPhotoIds(),
							add.getApartment().getUniversity().getUniversityId(),
							add.getApartment().getUniversity().getUniversityName(), universityPhoto,
							add.getApartment().getUniversity().getUnivAcronym(), add.getApartment().getCity(),
							add.getApartment().getState(), add.getApartment().getZip()));
				}
			} else {

				rAdds.add(new RAccommodationAdd(add.getVacancies(), add.getGender(), add.getNoOfRooms(), add.getCost(),
						add.getFbId(), add.getNotes(), user.getUserId(), add.getApartment().getApartmentName(),
						user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), add.getAddId(),
						true, new SimpleDateFormat("dd MMM").format(add.getDatePosted()), add.getAddPhotoIds(),
						add.getApartment().getUniversity().getUniversityId(),
						add.getApartment().getUniversity().getUniversityName(), universityPhoto,
						add.getApartment().getUniversity().getUnivAcronym(), add.getApartment().getCity(),
						add.getApartment().getState(), add.getApartment().getZip()));

			}

		}

		return rAdds;

	}

	public List<UniversityAccommodationDTO> getSimpleSearchAddsNg(AccommodationSearchDTO accommodationSearch) {

		accommmodationDAO.getSimpleSearchAddsNg(accommodationSearch);

		return null;
	}

	public int addNewApartment(Apartments apartment, int universityId) throws Exception {

		return accommmodationDAO.addNewApartment(apartment, universityId);
	}

}
