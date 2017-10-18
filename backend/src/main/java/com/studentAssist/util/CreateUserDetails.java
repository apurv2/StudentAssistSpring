package com.studentAssist.util;

import java.util.List;

public class CreateUserDetails{

	String instanceId;
	String registrationId;
	List<Integer> selectedUniversityIds;

	public CreateUserDetails()
	{}
	
	
	public CreateUserDetails(String instanceId, String registrationId, List<Integer> selectedUniversityIds) {
		this.instanceId = instanceId;
		this.registrationId = registrationId;
		this.selectedUniversityIds = selectedUniversityIds;
	}

	

	public String getInstanceId() {
		return instanceId;
	}

	public List<Integer> getSelectedUniversityIds() {
		return selectedUniversityIds;
	}


	public void setSelectedUniversityIds(List<Integer> selectedUniversityIds) {
		this.selectedUniversityIds = selectedUniversityIds;
	}


	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

}
