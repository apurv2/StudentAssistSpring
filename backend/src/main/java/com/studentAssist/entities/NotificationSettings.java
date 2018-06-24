package com.studentAssist.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class NotificationSettings implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private long checkerUserId;

	@ManyToOne
	@JoinColumn(name = "UNIV_ID")
	private Universities university;

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
		this.university = university;
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

	public Universities getUniversity() {
		return university;
	}

	public void setUniversity(Universities university) {
		this.university = university;
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

	public long getCheckerUserId() {
		return checkerUserId;
	}

	public void setCheckerUserId(long checkerUserId) {
		this.checkerUserId = checkerUserId;
	}

	@Override
	public String toString() {
		return "NotificationTest [apartmentName=" + apartmentName + ", gender=" + gender + ", apartmentType="
				+ apartmentType + "]";
	}

}
