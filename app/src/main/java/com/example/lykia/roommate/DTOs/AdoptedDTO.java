package com.example.lykia.roommate.DTOs;

import java.sql.Timestamp;

public class AdoptedDTO {

    private int adoptedId;
    private UserDTO fromUser;
    private UserDTO toUser;
    private RaceDTO race;
    private String imagePath;
    private String gender;
    private int monthOld;
    private Timestamp adoptedDate;

    public AdoptedDTO() {
    }

    public AdoptedDTO(int adoptedId, UserDTO fromUser, UserDTO toUser, RaceDTO race, String imagePath, String gender, int monthOld, Timestamp adoptedDate) {
        this.adoptedId = adoptedId;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.race = race;
        this.imagePath = imagePath;
        this.gender = gender;
        this.monthOld = monthOld;
        this.adoptedDate = adoptedDate;
    }

    public int getAdoptedId() {
        return adoptedId;
    }

    public void setAdoptedId(int adoptedId) {
        this.adoptedId = adoptedId;
    }

    public UserDTO getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserDTO fromUser) {
        this.fromUser = fromUser;
    }

    public UserDTO getToUser() {
        return toUser;
    }

    public void setToUser(UserDTO toUser) {
        this.toUser = toUser;
    }

    public RaceDTO getRace() {
        return race;
    }

    public void setRace(RaceDTO race) {
        this.race = race;
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

    public Timestamp getAdoptedDate() {
        return adoptedDate;
    }

    public void setAdoptedDate(Timestamp adoptedDate) {
        this.adoptedDate = adoptedDate;
    }
}
