package com.example.lykia.roommate.DTOs;

import java.sql.Date;

public class RehomingDTO {

    private int pet_id;
    private UserDTO owner_id;
    private RaceDTO race_id;
    private String code;
    private String image_path;
    private String gender;
    private int month_old;
    private String information;
    private Date addition_date;

    public int getPet_id() {
        return pet_id;
    }

    public void setPet_id(int pet_id) {
        this.pet_id = pet_id;
    }

    public UserDTO getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(UserDTO owner_id) {
        this.owner_id = owner_id;
    }

    public RaceDTO getRace_id() {
        return race_id;
    }

    public void setRace_id(RaceDTO race_id) {
        this.race_id = race_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getMonth_old() {
        return month_old;
    }

    public void setMonth_old(int month_old) {
        this.month_old = month_old;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Date getAddition_date() {
        return addition_date;
    }

    public void setAddition_date(Date addition_date) {
        this.addition_date = addition_date;
    }
}
