package com.example.lykia.roommate.DTOs;

import java.sql.Timestamp;

public class AdoptedDTO {

    private int adoptedId;
    private UserDTO fromUser;
    private UserDTO toUser;
    private String code;
    private Timestamp adoptedDate;

    public AdoptedDTO() {
    }

    public AdoptedDTO(int adoptedId, UserDTO fromUser, UserDTO toUser, String code, Timestamp adoptedDate) {
        this.adoptedId = adoptedId;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Timestamp getAdoptedDate() {
        return adoptedDate;
    }

    public void setAdoptedDate(Timestamp adoptedDate) {
        this.adoptedDate = adoptedDate;
    }
}
