package com.studentAssist.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class UserDummy {

	public UserDummy() {
	}

	@OneToOne(fetch = FetchType.EAGER)
	private Users user;

	@Id
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserDummy(Users userId) {
		super();
		this.user = userId;
	}

	public Users getUserId() {
		return user;
	}

	public void setUserId(Users userId) {
		this.user = userId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
}
