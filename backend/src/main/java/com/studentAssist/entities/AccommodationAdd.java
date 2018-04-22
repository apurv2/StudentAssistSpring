package com.studentAssist.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class AccommodationAdd {

	private String vacancies;
	private String gender;
	private String noOfRooms;
	private int cost;
	private String fbId;
	private String notes;
	private boolean delete_sw;
	private Date postedTill;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID")
	private Users user;

	@Id
	@GeneratedValue
	@Column(name = "ADD_ID")
	private int addId;

	@ManyToOne
	@JoinColumn(name = "APARTMENT_ID")
	private Apartments apartment;

	@ManyToOne
	@JoinColumn(name = "UNIVERSITY_ID")
	private Universities university;

	private Date datePosted;

	@OneToMany(mappedBy = "accommodationAdd", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserVisitedAdds> userVisitedAdds = new ArrayList();

	@OneToMany(mappedBy = "accommodationAdd", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserAccommodationNotifications> userNotifications = new ArrayList();

	@ElementCollection
	private List<String> addPhotoIds = new ArrayList<String>();

	public Universities getUniversity() {
		return university;
	}

	public void setUniversity(Universities university) {
		this.university = university;
	}

	public List<String> getAddPhotoIds() {
		return addPhotoIds;
	}

	public void setAddPhotoIds(List<String> addPhotoIds) {
		this.addPhotoIds = addPhotoIds;
	}

	public AccommodationAdd() {
	}

	public AccommodationAdd(String vacancies, String gender, String noOfRooms, int cost, String fbId, String notes,
							Date datePosted, List<String> apartmentPictureId) {
		this.vacancies = vacancies;
		this.gender = gender;
		this.noOfRooms = noOfRooms;
		this.cost = cost;
		this.fbId = fbId;
		this.notes = notes;
		this.datePosted = datePosted;
		this.addPhotoIds = apartmentPictureId;

	}

	public List<UserAccommodationNotifications> getUserNotifications() {
		return userNotifications;
	}

	public void setUserNotifications(List<UserAccommodationNotifications> userNotifications) {
		this.userNotifications = userNotifications;
	}

	public AccommodationAdd(int addId) {
		this.addId = addId;
	}

	public Apartments getApartment() {
		return this.apartment;
	}

	public void setApartment(Apartments apartment) {
		this.apartment = apartment;
	}

	public String getVacancies() {
		return this.vacancies;
	}

	public void setVacancies(String vacancies) {
		this.vacancies = vacancies;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNoOfRooms() {
		return this.noOfRooms;
	}

	public void setNoOfRooms(String noOfRooms) {
		this.noOfRooms = noOfRooms;
	}

	public int getCost() {
		return this.cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getFbId() {
		return this.fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public int getAddId() {
		return this.addId;
	}

	public void setAddId(int addId) {
		this.addId = addId;
	}

	public Date getDatePosted() {
		return this.datePosted;
	}

	public void setDatePosted(Date datePosted) {
		this.datePosted = datePosted;
	}

	public List<UserVisitedAdds> getUserVisitedAdds() {
		return userVisitedAdds;
	}

	public void setUserVisitedAdds(List<UserVisitedAdds> userVisitedAdds) {
		this.userVisitedAdds = userVisitedAdds;
	}

	public Date getPostedTill() {
		return postedTill;
	}

	public void setPostedTill(Date postedTill) {
		this.postedTill = postedTill;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datePosted == null) ? 0 : datePosted.hashCode());
		return result;
	}

	public boolean isDelete_sw() {
		return delete_sw;
	}

	public void setDelete_sw(boolean delete_sw) {
		this.delete_sw = delete_sw;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccommodationAdd other = (AccommodationAdd) obj;
		if (datePosted == null) {
			if (other.datePosted != null)
				return false;
		} else if (!datePosted.equals(other.datePosted))
			return false;
		return true;
	}
}
