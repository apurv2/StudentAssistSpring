package com.studentAssist.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UniversityPhotos {

	@Id
	@GeneratedValue
	private int photoId;

	@ManyToOne
	@JoinColumn(name = "universityId")
	private Universities universityId;

	private int photoPriority;

	private String photoUrl;

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}

	public Universities getUniversityId() {
		return universityId;
	}

	public void setUniversityId(Universities universityId) {
		this.universityId = universityId;
	}

	public int getPhotoPriority() {
		return photoPriority;
	}

	public void setPhotoPriority(int photoPriority) {
		this.photoPriority = photoPriority;
	}

}
