package com.example.lykia.roommate.DTOs;

import java.sql.Date;

public class UserDTO {

    private int userId;
    private String mail;
    private String imagePath;
    private String firstName;
    private String lastName;
    private String location;
    private String password;
    private Date registerDate;

    public UserDTO() {
    }

    public UserDTO(String mail, String imagePath, String firstName, String lastName, String location, String password) {
        this.mail = mail;
        this.imagePath = imagePath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}
