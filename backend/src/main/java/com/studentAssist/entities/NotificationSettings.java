package com.studentAssist.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class NotificationSettings implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String checkerUserId;

	@ManyToOne
	@JoinColumn(name = "UNIV_ID")
	private Universities universityId;

	public String getCheckerUserId() {
		return checkerUserId;
	}

	public void setCheckerUserId(String checkerUserId) {
		this.checkerUserId = checkerUserId;
	}

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private Users user;

	@ElementCollection
	private List<String> apartmentName = new ArrayList<String>();

	private String gender;

	@ElementCollection
	private List<String> apartmentType = new ArrayList<String>();

	private Date createDate;

	public NotificationSettings() {
	}

	public List<String> getApartmentName() {
		return apartmentName;
	}

	public NotificationSettings(Users user, List<String> apartmentName, String gender, List<String> apartmentType,
			Universities university) {
		this.universityId = university;
		this.user = user;
		this.apartmentName = apartmentName;
		this.gender = gender;
		this.apartmentType = apartmentType;
	}

	public NotificationSettings(Users user, List<String> apartmentName, String gender, List<String> apartmentType) {
		this.user = user;
		this.apartmentName = apartmentName;
		this.gender = gender;
		this.apartmentType = apartmentType;
	}

	public Users getUser() {
		return user;
	}

	public Universities getUniversityId() {
		return universityId;
	}

	public void setUniversityId(Universities universityId) {
		this.universityId = universityId;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setApartmentName(List<String> apartmentName) {
		this.apartmentName = apartmentName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<String> getApartmentType() {
		return apartmentType;
	}

	public void setApartmentType(List<String> apartmentType) {
		this.apartmentType = apartmentType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "NotificationTest [apartmentName=" + apartmentName + ", gender=" + gender + ", apartmentType="
				+ apartmentType + "]";
	}

}
