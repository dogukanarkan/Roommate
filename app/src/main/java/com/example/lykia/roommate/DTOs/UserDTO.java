package com.example.lykia.roommate.DTOs;

import java.sql.Date;

public class UserDTO {

    private int user_id;
    private String mail;
    private String image_path;
    private String first_name;
    private String last_name;
    private String location;
    private String password;
    private Date register_date;

    public UserDTO() {
    }

    public UserDTO(String mail, String image_path, String first_name, String last_name, String location, String password) {
        this.mail = mail;
        this.image_path = image_path;
        this.first_name = first_name;
        this.last_name = last_name;
        this.location = location;
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public Date getRegister_date() {
        return register_date;
    }

    public void setRegister_date(Date register_date) {
        this.register_date = register_date;
    }
}
