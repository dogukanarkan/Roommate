package com.example.lykia.roommate.DTOs;

public class RaceDTO {

    private int race_id;
    private AnimalDTO animal_id;
    private String race_name;

    public RaceDTO(AnimalDTO animal_id, String race_name) {
        this.animal_id = animal_id;
        this.race_name = race_name;
    }

    public int getRace_id() {
        return race_id;
    }

    public void setRace_id(int race_id) {
        this.race_id = race_id;
    }

    public AnimalDTO getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(AnimalDTO animal_id) {
        this.animal_id = animal_id;
    }

    public String getRace_name() {
        return race_name;
    }

    public void setRace_name(String race_name) {
        this.race_name = race_name;
    }
}
