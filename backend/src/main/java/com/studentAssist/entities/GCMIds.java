package com.studentAssist.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GCMIds implements Serializable {
	@Id
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private Users user;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((gcmId == null) ? 0 : gcmId.hashCode());
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
		GCMIds other = (GCMIds) obj;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (gcmId == null) {
			if (other.gcmId != null)
				return false;
		} else if (!gcmId.equals(other.gcmId))
			return false;
		return true;
	}

	String gcmId;
	@Id
	String deviceId;
	private Date createDate;

	
	
	public GCMIds() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getGcmId() {
		return gcmId;
	}

	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public GCMIds(String gcmId, String deviceId, Date createDate) {
		this.gcmId = gcmId;
		this.deviceId = deviceId;
		this.createDate = createDate;
	}

}
