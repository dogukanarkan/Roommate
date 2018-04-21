package com.example.lykia.roommate.DTOs;

import java.sql.Date;

public class RehomingDTO {

    private int petId;
    private UserDTO user;
    private RaceDTO race;
    private String code;
    private String imagePath;
    private String gender;
    private int monthOld;
    private String information;
    private Date additionDate;

    public RehomingDTO() {
    }

    public RehomingDTO(UserDTO user, RaceDTO race, String code, String imagePath, String gender, int monthOld, String information) {
        this.user = user;
        this.race = race;
        this.code = code;
        this.imagePath = imagePath;
        this.gender = gender;
        this.monthOld = monthOld;
        this.information = information;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public RaceDTO getRace() {
        return race;
    }

    public void setRace(RaceDTO race) {
        this.race = race;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getMonthOld() {
        return monthOld;
    }

    public void setMonthOld(int monthOld) {
        this.monthOld = monthOld;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Date getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(Date additionDate) {
        this.additionDate = additionDate;
    }
}
