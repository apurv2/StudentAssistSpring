package com.studentAssist.util;

import java.util.HashMap;
import java.util.Map;

public class SAConstants {
	public static final String welcome = "Welcome!";
	public static final String STUDENT_ASSIST = "Student Assist!";
	public static final String sharedPreferenceName = "sharedPreference";
	public static final String feedbackEmailAddress = "apurv2@gmail.com";
	public static final String feedbackEmailSubject = "Feedback for student Assist";
	public static final String EmptyText = "";
	public static final String plainText = "plain/text";
	public static final String[] APARTMENT_TYPES = { "On-Campus", "Off-Campus", "Dorms" };
	public static final String[] MALE_FEMALE = { "Male", "Female", "Doesnt Matter" };
	public static final String APARTMENT_TYPE_SPINNER_INFORMATION = "Apartment Type";
	public static final String APARTMENT_NAME_SPINNER_INFORMATION = "Apartment Name";
	public static final String GENDER_SPINNER_INFORMATION = "Gender";
	public static final String GET_APARTMENT_NAMES_URL = "http://omega.uta.edu/~hxj3300/getAllApartmentNames.php";
	public static final String APARTMENT_NAME = "apartmentName";
	public static final String URL = "http://sassist-9vtjvsmaju.elasticbeanstalk.com/rest/accommodation";
	public static final String SEARCH_TYPE_ID = "searchTypeId";
	public static final String[] pageTitles = { "SEARCH VACANCY", "POST VACANCY", "YOUR POSTS,ADVANCED SEARCH" };
	public static final String ACCOMMODATION_ADDS = "AccommodationAdds";
	public static final String APARTMENT_NAMES = "apartmentNames";
	public static final String TAB_POSITION = "tab_position";
	public static final String ACCOMMODATION_ADD = "accommodationAdd";
	public static final String LEASE_TRANSFER = "Lease Transfer";
	public static final String USER = "user";
	public static final String G_FIRST_NAME = "first_name";
	public static final String G_EMAIL = "first_name";
	public static final String G_LAST_NAME = "last_name";
	public static final String G_ID = "id";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String EMAIL_ID = "emailId";
	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String VACANCIES = "vacancies";
	public static final String NO_OF_ROOMS = "noOfRooms";
	public static final String LOOKING_FOR = "lookingFor";
	public static final String COST = "cost";
	public static final String USER_ID = "userId";
	public static final String ADD_ID = "addId";
	public static final String FB_ID = "fbId";
	public static final String NOTES = "notes";
	public static final String ACCOMMODATION_ADD_PARCELABLE = "accommodationAddParcelable";
	public static final String RECENTS = "recents";
	public static final String STATE_CHANGED = "onStateChanged";
	public static final String PROFILE_PIC = "profilePic";
	public static final String ALL = "all";
	public static final String GENDER = "gender";
	public static final String USER_POSTS = "Your Posts";
	public static final String APARTMENT_TYPE = "apartmentType";
	public static final String RESPONSE_SUCCESS = "{\"response\":\"success\"}";
	public static final String POST_ACCOMMODATION = "Post Your Accommodation";
	public static final String POST_ADD = "post_add";
	public static final String OTHER = "other";
	public static final String APARTMENT_NAME_PROMPT = "please enter a valid Apartment Name.";
	public static final String COST_OF_LIVING_NULL_CHECK = "Cost of Living cannot be empty.";
	public static final String NO_OF_VACANCIES = "noOfVacancies";
	public static final String SUCCESSFULLY_POSTED = "Successfully Posted!!";
	public static final String POST_FAILURE = "Sorry, we could not post your need. Please try again later!!";
	public static final String RESPONSE_FAILURE = "{\"response\":\"failure\"}";
	public static final String FAILURE = "failure";
	public static final String NOTIFICATION_TYPE = "notificationType";
	public static final String SEARCH_TYPE = "searchType";
	public static final String SEARCH_VALUE = "searchValue";
	public static final String POSITION = "position";
	public static final String NOTIFICATIONID = "notificationId";
	public static final String JSON_DATA = "jsonData";
	public static final String LEFT_SPINNER = "leftSpinner";
	public static final String RIGHT_SPINNER = "rightSpinner";
	public static final String SEARCH_ID = "searchId";
	public static final String GCM_ID = "gcmId";
	public static final String APP_VERSION = "appVersion";
	public static final String REGISTRATION_ID = "registrationId";
	public static final String ALERT_TEXT = "alertText";
	public static final String NOTIFICATION_TEXT_APARTMENT = "We have an apartment for you!!";
	public static final String NOT_REGISTERED_USER = "not registered";
	public static final String REGISTERED_USER_WITHOUT_FB_ID = "registered user without fb-id";
	public static final String REGISTERED_USER_WITH_FB_ID = "registered user with fb-id";
	public static final String LOADER_TEXT = "loaderText";
	public static final String REQUESTING_SUBSCTIPTION = "Requesting Subscription";
	public static final String POSTING_REQUEST = "Posting Request";
	public static final String ERROR = "error";
	public static final String FETCH = "fetch";
	public static final String GROUP_NAME = "groupName";
	public static final String GROUP_LINK = "groupLink";
	public static final String GROUP_DESCRIPTION = "groupDescription";
	public static final String DIALOG = "dialog";
	public static final String APARTMENT_NAME_POSITION = "apartment names Position";
	public static final String VOLLEY_ERROR = "Error in connection, please try again";
	public static final String PHONE_LIST = "phoneList";
	public static final String SUCCESSFULLY_DELETED = "Successfully Deleted";
	public static final String ALREADY_SUBSCRIBED = "already Subscribed";
	public static final String SPINNER1 = "spinner1";
	public static final String SPINNER2 = "spinner2";
	public static final String SIMPLE_NOTIFICATIONS = "simpleNotifications";
	public static final String ADVANCED_NOTIFICATIONS = "advancedNotifications";
	public static final String DRAWER_LEARNT = "drawer learnt";
	public static final String LEARNT = "LEARNT";
	public static final String GCM_KEY = "AIzaSyDGnCgvHV1vDQW-WyDFnVZOuZTZ4X_uNLA";
	public static final String GET = "get";
	public static final String VIEW_PAGER_POSITION = "position";
	public static final String APARTE = "position";
	public static final String ON_CAMPUS = "on";
	public static final String OFF_CAMPUS = "off";
	public static final String Dorms = "dorms";
	public static final String ACCESS_TOKEN = "accessToken";
	public static final String INSTANCE_ID = "instanceId";
	public static final String USER_HAS_UNIVS = "{\"response\":\"true\"}";
	public static final String USER_DOES_NOT_HAVE_UNIVS = "{\"response\":\"false\"}";

	public static final HashMap<String, String> apartmentTypeCodeMap = new HashMap<String, String>();
	public static final int PAGE_SIZE = 10;
	public static final String UNAUTHORIZED = "Unauthorized";
	public static final String RESPONSE = "response";
	public static final String RESPONSE_EXCEPTION = "Sorry we could not process your request at this time.";

	public static final Map<Long, Long> ADMIN_USER_ID = new HashMap<>();

	static {
		ADMIN_USER_ID.put(10207639158073180L, 10207639158073180L); // APURV
		ADMIN_USER_ID.put(100002047925455L, 100002047925455L); // KIRAN
	}

	static {
		apartmentTypeCodeMap.put("on", "On-Campus");
		apartmentTypeCodeMap.put("off", "Off-Campus");
		apartmentTypeCodeMap.put("dorms", "Dorms");
	}

}
