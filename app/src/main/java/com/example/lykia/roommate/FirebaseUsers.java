package com.example.lykia.roommate;

public class FirebaseUsers {

    private String name;
    private String image;

    public FirebaseUsers() {
    }

    public FirebaseUsers(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public FirebaseUsers(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
