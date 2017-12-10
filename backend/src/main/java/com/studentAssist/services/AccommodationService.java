
package com.studentAssist.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.studentAssist.dao.AccommodationDAO;
import com.studentAssist.entities.AccommodationAdd;
import com.studentAssist.entities.Apartments;
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
		user.setUserId(userId);

		user.setRegisteredDate(Utilities.getDate());

		AccommodationAdd advertisement = new AccommodationAdd(vacancies, gender, noOfRooms, cost, fbId, notes,
				Utilities.getDate(), addPhotoIds);

		return accommmodationDAO.createAccommodationAddFromFacebook(user, advertisement, apartmentName);

	}

	public List<RAccommodationAdd> getAccommodationNotifications(Users user, int position) throws Exception {

		List<AccommodationAdd> accommodationAdds;
		accommodationAdds = accommmodationDAO.getAccommodationNotifications(user, position);

		List<Long> addIds = getUserVisitedAdds(user);
		return getRAccommodationAdds(accommodationAdds, addIds);
	}

	public String createAccommodationAdd(String userId, String apartmentName, String noOfRooms, String vacancies,
			String cost, String gender, String fbId, String notes, List<String> addPhotoIds) throws Exception {

		AccommodationAdd advertisement = new AccommodationAdd(vacancies, gender, noOfRooms, cost, fbId, notes,
				Utilities.getDate(), addPhotoIds);

		return accommmodationDAO.createAccommodationAdd(userId, advertisement, apartmentName);
	}

	public String deleteAccommodationAdd(long addId) throws Exception {

		Cloudinary cloudinary;
		cloudinary = new Cloudinary("cloudinary://647816789382186:5R3U1Oc9zwvnPOfI-TtlIeI0u_E@duf1ntj7z");

		AccommodationAdd add = accommmodationDAO.getByKey(AccommodationAdd.class, addId);
		if (add != null) {

			accommmodationDAO.deleteAccommodationAdd(add);
		}
		for (String url : add.getAddPhotoIds()) {

			String[] params = url.split("/");
			String cloudId = params[params.length - 1].split("\\.")[0];
			Map map = cloudinary.uploader().destroy(cloudId, Cloudinary.emptyMap());

			System.out.println("map==" + map);
		}
		return SAConstants.RESPONSE_SUCCESS;
	}

	public List<RAccommodationAdd> getUserPosts(String userId, int position) throws Exception {

		List<AccommodationAdd> userAdds = accommmodationDAO.getUserPosts(userId, position);
		return getRAccommodationAdds(userAdds, null);

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

	public List<RAccommodationAdd> getAdvancedAdvertisements(String apartmentName, String gender, Users currentUser,
			int position) throws Exception {

		List<AccommodationAdd> advancedSearchAdds = accommmodationDAO.getAdvancedAdvertisements(apartmentName, gender,
				position);

		List<Long> addIds = getUserVisitedAdds(currentUser);

		return getRAccommodationAdds(advancedSearchAdds, addIds);

	}

	public List<RAccommodationAddJson> getSimpleSearchAddsForWebApp(String leftSpinner, String rightSpinner,
			Users currentUser) throws Exception {

		List<AccommodationAdd> simpleSearchAdds = accommmodationDAO.getSimpleSearchAdds(leftSpinner, rightSpinner,
				currentUser);
		List<Long> addIds = getUserVisitedAdds(currentUser);
		List<RAccommodationAdd> accomodationAdds = getRAccommodationAdds(simpleSearchAdds, addIds);

		List<Integer> universityIds = new ArrayList<>();
		// List<RAccommodationAddJson> addJson = new ArrayList<>();
		for (AccommodationAdd add : simpleSearchAdds) {
			if (!(universityIds.contains(add.getApartment().getUniversity().getUniversityId()))) {
				universityIds.add(add.getApartment().getUniversity().getUniversityId());
			}
		}

		List<RAccommodationAddJson> accoAddsJson = new ArrayList<>();
		List<RAccommodationAdd> accoAdds = new ArrayList<>();
		for (int universityId : universityIds) {
			for (RAccommodationAdd add : accomodationAdds) {
				if (universityId == add.getUniversityId()) {
					accoAdds.add(add);
				}
			}
			List<RAccommodationAdd> uniAdds = new ArrayList<>();
			uniAdds.addAll(accoAdds);
			accoAddsJson.add(new RAccommodationAddJson(universityId, uniAdds));
			accoAdds.clear();
		}

		return accoAddsJson;

	}

	public List<RAccommodationAdd> getSimpleSearchAdds(String leftSpinner, String rightSpinner, Users currentUser)
			throws Exception {

		List<AccommodationAdd> simpleSearchAdds = accommmodationDAO.getSimpleSearchAdds(leftSpinner, rightSpinner,
				currentUser);
		List<Long> addIds = getUserVisitedAdds(currentUser);
		List<RAccommodationAdd> accomodationAdds = getRAccommodationAdds(simpleSearchAdds, addIds);

		return accomodationAdds;

	}

	public List<RAccommodationAdd> getSimpleSearchAddsPagination(String leftSpinner, String rightSpinner, int position,
			int universityId) {

		List<AccommodationAdd> simpleSearchAdds = accommmodationDAO.getSimpleSearchAddsPagination(leftSpinner,
				rightSpinner, position, universityId);
		return getRAccommodationAdds(simpleSearchAdds, null);
	}

	public List<RApartmentNames> getApartmentNames(String apartmentType) throws Exception {

		List<Apartments> apartments = accommmodationDAO.getApartmentNames(apartmentType);
		List<RApartmentNames> rApartments = new ArrayList<>();
		for (Apartments apt : apartments) {
			rApartments.add(new RApartmentNames(apt.getApartmentName()));
		}
		return rApartments;
	}

	public String addNewApartment(String apartmentName, String apartmentType) throws Exception {

		return accommmodationDAO.addNewApartment(apartmentName, apartmentType);
	}

	public List<RApartmentNamesWithType> getApartmentNamesWithType() throws Exception {

		List<Apartments> apartments = accommmodationDAO.getApartmentNamesWithType();
		List<RApartmentNamesWithType> rApartments = new ArrayList<>();

		for (Apartments apartment : apartments) {
			rApartments.add(new RApartmentNamesWithType(apartment.getApartmentName(), apartment.getApartmentType()));
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

		return getRAccommodationAdds(accommodationAdds, null);

	}

	public List<RAccommodationAdd> getRAccommodationAdds(List<AccommodationAdd> accommodationAdds, List<Long> addIds) {

		List<RAccommodationAdd> rAdds = new ArrayList<RAccommodationAdd>();

		for (AccommodationAdd add : accommodationAdds) {

			String universityPhoto = SAConstants.EmptyText;
			String universityLogo = SAConstants.EmptyText;
			List<UniversityPhotos> photos = add.getApartment().getUniversity().getUniversityPhotos();
			for (UniversityPhotos photo : photos) {
				if (photo.getPhotoPriority() == 2) {
					universityPhoto = photo.getPhotoUrl();
				} else if (photo.getPhotoPriority() == 1) {
					universityLogo = photo.getPhotoUrl();
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
						add.getApartment().getUniversity().getUniversityName(), universityLogo,
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

}
