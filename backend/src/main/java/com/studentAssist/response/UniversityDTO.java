package com.studentAssist.response;

import java.util.List;

public class UniversityDTO {

    String universityName;
    int universityId;
    List<ApartmentDTO> apartments;

    public UniversityDTO() {
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public int getUniversityId() {
        return universityId;
    }

    public List<ApartmentDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<ApartmentDTO> apartments) {
        this.apartments = apartments;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

}
