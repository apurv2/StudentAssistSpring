package com.studentAssist.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Universities {

	@Id
	@GeneratedValue
	private int universityId;

	private String universityName;

	private String description;

	private String addr_line;

	private String city;

	private String state;

	private int zip;

	private int estdYear;

	private String univAcronym;

	@OneToMany(mappedBy = "universityId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<NotificationSettings> notificationSettings = new ArrayList<>();

	@ElementCollection
	private List<String> universityPhotosUrls = new ArrayList<String>();

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "universities")
	private List<Users> users = new ArrayList<>();

	@OneToMany(mappedBy = "university", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Apartments> apartments = new ArrayList<>();

	@OneToMany(mappedBy = "universityId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UniversityPhotos> universityPhotos = new ArrayList<UniversityPhotos>();

	private Date dateAdded;

	public List<NotificationSettings> getNotificationSettings() {
		return notificationSettings;
	}

	public void setNotificationSettings(List<NotificationSettings> notificationSettings) {
		this.notificationSettings = notificationSettings;
	}

	public List<UniversityPhotos> getUniversityPhotos() {
		return universityPhotos;
	}

	public void setUniversityPhotos(List<UniversityPhotos> universityPhotos) {
		this.universityPhotos = universityPhotos;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public int getUniversityId() {
		return universityId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getUniversityPhotosUrls() {
		return universityPhotosUrls;
	}

	public void setUniversityPhotosUrls(List<String> universityPhotosUrls) {
		this.universityPhotosUrls = universityPhotosUrls;
	}

	public List<Apartments> getApartments() {
		return apartments;
	}

	public void setApartments(List<Apartments> apartments) {
		this.apartments = apartments;
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

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}

	public int getEstdYear() {
		return estdYear;
	}

	public void setEstdYear(int estdYear) {
		this.estdYear = estdYear;
	}

	public String getUnivAcronym() {
		return univAcronym;
	}

	public void setUnivAcronym(String univAcronym) {
		this.univAcronym = univAcronym;
	}

	public String getAddr_line() {
		return addr_line;
	}

	public void setAddr_line(String addr_line) {
		this.addr_line = addr_line;
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

}