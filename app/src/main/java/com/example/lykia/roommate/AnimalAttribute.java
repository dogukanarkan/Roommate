package com.example.lykia.roommate;

/**
 * Created by hilal on 1.05.2018.
 */

public class AnimalAttribute {
    private String genus;
    private int photo_id;

    public String getGenus()
    {
        return this.genus;
    }

    public int getPhoto_id()
    {
        return this.photo_id;
    }

    public AnimalAttribute(String genus,int photo_id)
    {
        this.genus = genus;
        this.photo_id = photo_id;
    }
    public AnimalAttribute()
    {

    }
}
