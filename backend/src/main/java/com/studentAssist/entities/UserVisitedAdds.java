package com.studentAssist.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserVisitedAdds implements Serializable {

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

	public UserVisitedAdds(Users user, AccommodationAdd accommodationAdd) {
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

	public UserVisitedAdds() {

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accommodationAdd == null) ? 0 : accommodationAdd.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		UserVisitedAdds other = (UserVisitedAdds) obj;
		if (accommodationAdd == null) {
			if (other.accommodationAdd != null)
				return false;
		} else if (!accommodationAdd.equals(other.accommodationAdd))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
