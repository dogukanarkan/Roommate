package com.example.lykia.roommate.DTOs;

public class RaceDTO {

    private int race_id;
    private AnimalDTO animal;
    private String race_name;

    public RaceDTO(AnimalDTO animal, String race_name) {
        this.animal = animal;
        this.race_name = race_name;
    }

    public int getRace_id() {
        return race_id;
    }

    public void setRace_id(int race_id) {
        this.race_id = race_id;
    }

    public AnimalDTO getAnimal() {
        return animal;
    }

    public void setAnimal(AnimalDTO animal) {
        this.animal = animal;
    }

    public String getRace_name() {
        return race_name;
    }

    public void setRace_name(String race_name) {
        this.race_name = race_name;
    }
}
