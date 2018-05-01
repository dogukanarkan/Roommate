package com.example.lykia.roommate;

public class FirebaseUsers {

    private String name;
    private String image;
    private String thumbImage;

    public FirebaseUsers() {
    }

    public FirebaseUsers(String name, String image, String thumbImage) {
        this.name = name;
        this.image = image;
        this.thumbImage = thumbImage;
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

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }
}
