package com.studentAssist.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserAccommodationNotifications implements Serializable {

	@ManyToOne
	@Id
	@JoinColumn(name = "USER_ID")
	private Users user;

	@ManyToOne
	@Id
	@JoinColumn(name = "ADD_ID")
	private AccommodationAdd accommodationAdd;
	
	
	public Users getUser() {
		return user;
	}

	public UserAccommodationNotifications(Users user, AccommodationAdd accommodationAdd) {
		super();
		this.user = user;
		this.accommodationAdd = accommodationAdd;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public AccommodationAdd getAccommodationAdd() {
		return accommodationAdd;
	}

	public void setAccommodationAdd(AccommodationAdd accommodationAdd) {
		this.accommodationAdd = accommodationAdd;
	}

	private Date createDate;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public UserAccommodationNotifications() {

	}

	
}
