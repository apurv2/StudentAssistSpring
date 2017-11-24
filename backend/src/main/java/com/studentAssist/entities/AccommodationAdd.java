package com.studentAssist.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class AccommodationAdd {

	private String vacancies;
	private String gender;
	private String noOfRooms;
	private String cost;
	private String fbId;
	private String notes;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID")
	private Users user;

	@Id
	@GeneratedValue
	@Column(name = "ADD_ID")
	private long addId;

	@ManyToOne
	@JoinColumn(name = "APARTMENT_ID")
	private Apartments apartment;

	private Date datePosted;

	@OneToMany(mappedBy = "accommodationAdd", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserVisitedAdds> userVisitedAdds = new ArrayList();

	@OneToMany(mappedBy = "accommodationAdd", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserAccommodationNotifications> userNotifications = new ArrayList();

	@ElementCollection
	private List<String> addPhotoIds = new ArrayList<String>();

	public List<String> getAddPhotoIds() {
		return addPhotoIds;
	}

	public void setAddPhotoIds(List<String> addPhotoIds) {
		this.addPhotoIds = addPhotoIds;
	}

	public AccommodationAdd() {
	}

	public AccommodationAdd(String vacancies, String gender, String noOfRooms, String cost, String fbId, String notes,
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

	public String getCost() {
		return this.cost;
	}

	public void setCost(String cost) {
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

	public long getAddId() {
		return this.addId;
	}

	public void setAddId(long addId) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datePosted == null) ? 0 : datePosted.hashCode());
		return result;
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