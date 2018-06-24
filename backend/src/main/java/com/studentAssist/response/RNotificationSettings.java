package com.studentAssist.response;

import java.util.ArrayList;
import java.util.List;

public class RNotificationSettings {

	private List<String> apartmentName = new ArrayList<String>();

	private String gender;

	int universityId;

	private String gcmId;

	private String instanceId;

	private List<String> apartmentType = new ArrayList<String>();

	private List<UniversityDTO> allUnivDetails = new ArrayList<UniversityDTO>();

	public RNotificationSettings(List<String> apartmentName, String gender, int universityId,
			List<String> apartmentType, List<UniversityDTO> apartmentNames) {
		super();
		this.apartmentName = apartmentName;
		this.gender = gender;
		this.universityId = universityId;
		this.apartmentType = apartmentType;
		this.allUnivDetails = apartmentNames;
	}

	public String getGcmId() {
		return gcmId;
	}

	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public List<String> getApartmentName() {
		return apartmentName;
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

	public List<UniversityDTO> getAllUnivDetails() {
		return allUnivDetails;
	}

	public void setAllUnivDetails(List<UniversityDTO> allUnivDetails) {
		this.allUnivDetails = allUnivDetails;
	}

	public List<String> getApartmentType() {
		return apartmentType;
	}

	public void setApartmentType(List<String> apartmentType) {
		this.apartmentType = apartmentType;
	}

	public int getUniversityId() {
		return universityId;
	}

	public void setUniversityId(int universityId) {
		this.universityId = universityId;
	}

	public RNotificationSettings() {
	}

	@Override
	public String toString() {
		return "RNotificationSettings [apartmentName=" + apartmentName + ", gender=" + gender + ", apartmentType="
				+ apartmentType + "]";
	}

}
