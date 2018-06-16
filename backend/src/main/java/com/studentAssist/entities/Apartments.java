package com.studentAssist.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Apartments")
public class Apartments {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    int id;
    String apartmentName;
    String apartmentType;
    Date addedDate;
    @OneToMany(mappedBy = "apartment")
    private List<AccommodationAdd> accommodationAdd = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private Users createdUser;

    @ManyToOne
    @JoinColumn(name = "universityId")
    private Universities university;

    private String addr_line;

    private String city;

    private String state;

    private int zip;

    public String getAddr_line() {
        return addr_line;
    }

    public void setAddr_line(String addr_line) {
        this.addr_line = addr_line;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public Universities getUniversity() {
        return university;
    }

    public void setUniversity(Universities university) {
        this.university = university;
    }

    public List<AccommodationAdd> getAccommodationAdd() {
        return this.accommodationAdd;
    }

    public void setAccommodationAdd(List<AccommodationAdd> accommodationAdd) {
        this.accommodationAdd = accommodationAdd;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApartmentName() {
        return this.apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getApartmentType() {
        return this.apartmentType;
    }

    public void setApartmentType(String apartmentType) {
        this.apartmentType = apartmentType;
    }

    public Date getAddedDate() {
        return this.addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Users getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(Users createdUser) {
        this.createdUser = createdUser;
    }
}
