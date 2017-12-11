package com.studentAssist.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Cloudinary;
import com.google.gson.Gson;
import com.studentAssist.entities.Users;
import com.studentAssist.exception.InvalidTokenException;
import com.studentAssist.response.AccommodationSearchDTO;
import com.studentAssist.response.RAccommodationAdd;
import com.studentAssist.response.RAccommodationAddJson;
import com.studentAssist.response.RApartmentNames;
import com.studentAssist.response.RApartmentNamesInUnivs;
import com.studentAssist.response.RApartmentNamesWithType;
import com.studentAssist.response.UniversityAccommodationDTO;
import com.studentAssist.services.AccommodationService;
import com.studentAssist.services.NotificationsService;
import com.studentAssist.util.InsertApartmentDetails;
import com.studentAssist.util.SAConstants;
import com.studentAssist.util.UserVisitedAddsRest;

@RestController
public class AccommodationController extends AbstractController {

	@Autowired
	private AccommodationService accommodationService;

	@Autowired
	private NotificationsService notificationService;

	@Autowired
	InsertApartmentDetails aptDetails;

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT, value = "/createAccommodationAddFromFacebook")
	public String createAccommodationAddFromFacebook(@RequestBody RAccommodationAdd rAccommodationAdd,
			HttpServletRequest request) throws Exception {

		Users user = getUserFromRequest(request);
		if (user.getUserId().equals("10207639158073180")) {

			return accommodationService.createAccommodationAddFromFacebook(rAccommodationAdd.getFbId(),
					rAccommodationAdd.getApartmentName(), rAccommodationAdd.getNoOfRooms(),
					rAccommodationAdd.getVacancies(), rAccommodationAdd.getCost(), rAccommodationAdd.getGender(),
					rAccommodationAdd.getFbId(), rAccommodationAdd.getNotes(), rAccommodationAdd.getFirstName(),
					rAccommodationAdd.getLastName(), rAccommodationAdd.getAddPhotoIds());

		} else {
			return null;
		}

	}

	/**
	 * Used in Accommodation Activity Notifications tab where user can see list
	 * of his notifications(list of accommodation Notifications only)
	 * 
	 * @param position
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getAccommodationNotifications")
	public List<RAccommodationAdd> getAccommodationNotifications(@RequestParam("position") int position,
			HttpServletRequest request) throws Exception {

		return accommodationService.getAccommodationNotifications(getUserFromRequest(request), position);

	}

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT, value = "/createAccommodationAdd")
	public String createAccommodationAdd(@RequestBody RAccommodationAdd rAccommodationAdd, HttpServletRequest request)
			throws Exception {

		Users user = getUserFromRequest(request);
		String userId = user.getUserId();

		return accommodationService.createAccommodationAdd(userId, rAccommodationAdd.getApartmentName(),
				rAccommodationAdd.getNoOfRooms(), rAccommodationAdd.getVacancies(), rAccommodationAdd.getCost(),
				rAccommodationAdd.getGender(), rAccommodationAdd.getFbId(), rAccommodationAdd.getNotes(),
				rAccommodationAdd.getAddPhotoIds());

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteAccommodationAdd")
	public String deleteAccommodationAdd(@RequestParam("addId") String addId) throws NumberFormatException, Exception {

		return accommodationService.deleteAccommodationAdd(Long.parseLong(addId));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getUserPosts")
	public List<RAccommodationAdd> getUserPosts(@RequestParam("position") int position, HttpServletRequest request)
			throws Exception {

		return accommodationService.getUserPosts(getUserFromRequest(request).getUserId(), position);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getAllApartmentNames")
	public List<RApartmentNames> getAllApartmentNames(HttpServletRequest request) throws Exception {

		return accommodationService.getAllApartmentNames(getUserFromRequest(request));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getAdvancedAdvertisements")
	public List<RAccommodationAdd> getAdvancedAdvertisements(@RequestParam("apartmentName") String apartmentName,
			@RequestParam("position") int position, @RequestParam("gender") String gender,
			@RequestParam("universityId") String universityId, HttpServletRequest request) throws Exception {

		return accommodationService.getAdvancedAdvertisements(apartmentName, gender, getUserFromRequest(request),
				position);

	}

	// @RequestMapping(method = RequestMethod.GET, value =
	// "/getSimpleSearchAddsAndroid")
	// public List<RAccommodationAdd>
	// getSimpleSearchAddsAndroid(@RequestParam("leftSpinner") String
	// leftSpinner,
	// @RequestParam("rightSpinner") String rightSpinner, HttpServletRequest
	// request) throws Exception {
	//
	// return accommodationService.getSimpleSearchAdds(leftSpinner,
	// rightSpinner, getUserFromRequest(request));
	// }

	// @RequestMapping(method = RequestMethod.GET, value =
	// "/getSimpleSearchAddsForWebApp")
	// public List<RAccommodationAddJson>
	// getSimpleSearchAddsForWebApp(@RequestParam("leftSpinner") String
	// leftSpinner,
	// @RequestParam("rightSpinner") String rightSpinner, HttpServletRequest
	// request) throws Exception {
	//
	// return accommodationService.getSimpleSearchAddsForWebApp(leftSpinner,
	// rightSpinner,
	// getUserFromRequest(request));
	// }

	/**
	 * Pagination for returning additional accommodation Adds
	 * 
	 * @param position
	 * @param universityId
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getSimpleSearchAddsPagination")
	public List<RAccommodationAdd> getSimpleSearchAddsPagination(@RequestParam("leftSpinner") String leftSpinner,
			@RequestParam("rightSpinner") String rightSpinner, @RequestParam("position") int position,
			@RequestParam("universityId") int universityId, HttpServletRequest request) {

		return accommodationService.getSimpleSearchAddsPagination(leftSpinner, rightSpinner, position, universityId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getApartmentNames")
	public List<RApartmentNames> getApartmentNames(@RequestParam("apartmentType") String apartmentType,
			HttpServletRequest request) throws Exception {

		return accommodationService.getApartmentNames(apartmentType);

	}

	/**
	 * Used to populate Spinners in advanced search
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getApartmentNamesByTypeAndUniv")
	public List<RApartmentNamesInUnivs> getApartmentNamesByTypeAndUniv(HttpServletRequest request) {

		return notificationService.getApartmentNamesWithTypeAndUniv(getUserFromRequest(request));

	}

	// code to be written in javascript
	@RequestMapping(method = RequestMethod.POST, value = "/addNewApartment")
	public String addNewApartment(@RequestParam("apartmentName") String apartmentName,
			@RequestParam("apartmentType") String apartmentType, HttpServletRequest request) throws Exception {

		return accommodationService.addNewApartment(apartmentName, apartmentType);

	}

	// javascript code to be added for accesstoken
	@RequestMapping(method = RequestMethod.GET, value = "/getAllApartmentsWithType")
	public List<RApartmentNamesWithType> getAllApartmentsWithType(HttpServletRequest request) throws Exception {

		return accommodationService.getApartmentNamesWithType();

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/setUserVisitedAdds")
	public String setUserVisitedAdds(@RequestBody UserVisitedAddsRest userVisited, HttpServletRequest request)
			throws NumberFormatException, Exception {

		String addId = userVisited.getAddId();

		if (addId != null && addId != "") {
			return accommodationService.setUserVisitedAdds(getUserFromRequest(request), Integer.parseInt(addId));
		}

		return SAConstants.RESPONSE_FAILURE;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/addAptsToDb")
	public String addAptsToDb(String test) {

		return aptDetails.addAptsToNewDb();

	}

	@RequestMapping(method = RequestMethod.GET, value = "/getRecentlyViewed")
	public List<RAccommodationAdd> getRecentlyViewed(@RequestParam("position") int position,
			HttpServletRequest request) {

		return accommodationService.getRecentlyViewed(getUserFromRequest(request), position);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/getSimpleSearchAdds")
	public List<UniversityAccommodationDTO> getSimpleSearchAddsUnregisteredUser(
			AccommodationSearchDTO accommodationSearch,
			HttpServletRequest request) throws Exception {

		return accommodationService.getSimpleSearchAdds(accommodationSearch);
	}

	@RequestMapping("/getCl")
	public String getCl(HttpServletRequest request) throws InvalidTokenException {

		String timestamp = (new Long(System.currentTimeMillis() / 1000L)).toString();

		Cloudinary cloudinary = new Cloudinary("cloudinary://647816789382186:5R3U1Oc9zwvnPOfI-TtlIeI0u_E@duf1ntj7z");

		Map<String, Object> params = new HashMap<String, Object>();
		Map options = Cloudinary.emptyMap();

		boolean returnError = Cloudinary.asBoolean(options.get("return_error"), false);

		String apiKey = Cloudinary.asString(options.get("api_key"), cloudinary.getStringConfig("api_key"));
		if (apiKey == null)
			throw new IllegalArgumentException("Must supply api_key");
		String apiSecret = Cloudinary.asString(options.get("api_secret"), cloudinary.getStringConfig("api_secret"));
		if (apiSecret == null)
			throw new IllegalArgumentException("Must supply api_secret"); //
		params.put("callback", //
				"");
		params.put("timestamp", timestamp);
		String expected_signature = cloudinary.apiSignRequest(params, apiSecret);

		System.out.println(timestamp);
		System.out.println(expected_signature);

		CloudinaryDetails details = new CloudinaryDetails(expected_signature, timestamp);
		Gson gson = new Gson();
		return gson.toJson(details);

	}

	private class CloudinaryDetails {
		String signature;
		String timeStamp;

		public String getSignature() {
			return signature;
		}

		public CloudinaryDetails(String signature, String timeStamp) {
			super();
			this.signature = signature;
			this.timeStamp = timeStamp;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}

		public String getTimeStamp() {
			return timeStamp;
		}

		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}

	}

}
