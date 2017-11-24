package com.studentAssist.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AccommodationAdd")
public class RAccommodationAdd {
	private String vacancies;
	private String gender;
	private String noOfRooms;
	private String cost;
	private String fbId;
	private String notes;
	private String userId;
	private String apartmentName;
	private String firstName;
	private String lastName;
	private String emailId;
	private String phoneNumber;
	private long addId;
	private boolean userVisitedSw;
	private String createDate;
	private int universityId;
	private String universityName;
	private String univAcronym;
	private String universityPhotoUrl;
	private String city;
	private String state;
	private int zip;

	public RAccommodationAdd(String vacancies, String gender, String noOfRooms, String cost, String fbId, String notes,
			String userId, String apartmentName, String firstName, String lastName, String emailId, String phoneNumber,
			long addId, boolean userVisitedSw, String createDate, List<String> apartmentPictureId, int universityId,
			String universityName, String universityPhotoUrl, String univAcronym, String city, String state, int zip) {
		super();
		this.vacancies = vacancies;
		this.gender = gender;
		this.noOfRooms = noOfRooms;
		this.cost = cost;
		this.fbId = fbId;
		this.notes = notes;
		this.userId = userId;
		this.apartmentName = apartmentName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.addId = addId;
		this.userVisitedSw = userVisitedSw;
		this.createDate = createDate;
		this.addPhotoIds = apartmentPictureId;
		this.universityId = universityId;
		this.universityName = universityName;
		this.universityPhotoUrl = universityPhotoUrl;
		this.univAcronym = univAcronym;
		this.city = city;
		this.state = state;
		this.zip = zip;

	}

	public RAccommodationAdd(String vacancies, String gender, String noOfRooms, String cost, String fbId, String notes,
			String userId, String apartmentName, String firstName, String lastName, String emailId, String phoneNumber,
			long addId, boolean userVisitedSw, String createDate, List<String> apartmentPictureId, int universityId,
			String universityName, String universityPhotoUrl, String univAcronym) {
		super();
		this.vacancies = vacancies;
		this.gender = gender;
		this.noOfRooms = noOfRooms;
		this.cost = cost;
		this.fbId = fbId;
		this.notes = notes;
		this.userId = userId;
		this.apartmentName = apartmentName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.addId = addId;
		this.userVisitedSw = userVisitedSw;
		this.createDate = createDate;
		this.addPhotoIds = apartmentPictureId;
		this.universityId = universityId;
		this.universityName = universityName;
		this.universityPhotoUrl = universityPhotoUrl;
		this.univAcronym = univAcronym;

	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getUnivAcronym() {
		return univAcronym;
	}

	public void setUnivAcronym(String univAcronym) {
		this.univAcronym = univAcronym;
	}

	public int getUniversityId() {
		return universityId;
	}

	public void setUniversityId(int universityId) {
		this.universityId = universityId;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	private List<String> addPhotoIds = new ArrayList<String>();

	public List<String> getAddPhotoIds() {
		return addPhotoIds;
	}

	public void setAddPhotoIds(List<String> addPhotoIds) {
		this.addPhotoIds = addPhotoIds;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public boolean getUserVisitedSw() {
		return userVisitedSw;
	}

	public void setUserVisitedSw(boolean userVisitedSw) {
		this.userVisitedSw = userVisitedSw;
	}

	public RAccommodationAdd(long addId) {
		this.addId = addId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	@XmlElement
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	@XmlElement
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return this.emailId;
	}

	@XmlElement
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	@XmlElement
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getVacancies() {
		return this.vacancies;
	}

	public RAccommodationAdd() {
	}

	@XmlElement
	public void setVacancies(String vacancies) {
		this.vacancies = vacancies;
	}

	public String getGender() {
		return this.gender;
	}

	@XmlElement
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNoOfRooms() {
		return this.noOfRooms;
	}

	@XmlElement
	public void setNoOfRooms(String noOfRooms) {
		this.noOfRooms = noOfRooms;
	}

	public String getCost() {
		return this.cost;
	}

	@XmlElement
	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getFbId() {
		return this.fbId;
	}

	@XmlElement
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getNotes() {
		return this.notes;
	}

	@XmlElement
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getUserId() {
		return this.userId;
	}

	@XmlElement
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getAddId() {
		return this.addId;
	}

	@XmlElement
	public void setAddId(long addId) {
		this.addId = addId;
	}

	public String getApartmentName() {
		return this.apartmentName;
	}

	@XmlElement
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public String getUniversityPhotoUrl() {
		return universityPhotoUrl;
	}

	public void setUniversityPhotoUrl(String universityPhotoUrl) {
		this.universityPhotoUrl = universityPhotoUrl;
	}

}