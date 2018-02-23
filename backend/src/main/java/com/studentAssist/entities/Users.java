package com.studentAssist.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Users {
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;

	@Column(insertable = true, updatable = false)
	private Date registeredDate;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<GCMIds> gcmIds = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserVisitedAdds> userVisitedAdds = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserAccommodationNotifications> userNotifications = new ArrayList<>();

	@Id
	private long userId;

	@OneToOne
	private UserDummy user_Id;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AccommodationAdd> accommodationAdd = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<NotificationSettings> notificationSettings = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_university", joinColumns = {
			@JoinColumn(name = "userId", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "universityId", nullable = false, updatable = false) })
	private List<Universities> universities = new ArrayList<>();

	public List<Universities> getUniversities() {
		return universities;
	}

	public UserDummy getUserDummy() {
		return user_Id;
	}

	public void setUserDummy(UserDummy userDummy) {
		this.user_Id = userDummy;
	}

	public void setUniversities(List<Universities> universities) {
		this.universities = universities;
	}

	public List<UserAccommodationNotifications> getUserNotifications() {
		return userNotifications;
	}

	public void setUserNotifications(List<UserAccommodationNotifications> userNotifications) {
		this.userNotifications = userNotifications;
	}

	public List<NotificationSettings> getNotificationSettings() {
		return notificationSettings;
	}

	public void setNotificationSettings(List<NotificationSettings> notificationSettings) {
		this.notificationSettings = notificationSettings;
	}

	public Date getRegisteredDate() {
		return this.registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public List<AccommodationAdd> getAccommodationAdd() {
		return this.accommodationAdd;
	}

	public void setAccommodationAdd(List<AccommodationAdd> accommodationAdd) {
		this.accommodationAdd = accommodationAdd;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<GCMIds> getGcmIds() {
		return gcmIds;
	}

	public void setGcmIds(List<GCMIds> gcmIds) {
		this.gcmIds = gcmIds;
	}

	public List<UserVisitedAdds> getUserVisitedAdds() {
		return userVisitedAdds;
	}

	public void setUserVisitedAdds(List<UserVisitedAdds> userVisitedAdds) {
		this.userVisitedAdds = userVisitedAdds;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
