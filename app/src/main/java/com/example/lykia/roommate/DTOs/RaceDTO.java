package com.example.lykia.roommate.DTOs;

public class RaceDTO {

    private int raceId;
    private AnimalDTO animal;
    private String raceName;

    public RaceDTO() {
    }

    public RaceDTO(AnimalDTO animal, String raceName) {
        this.animal = animal;
        this.raceName = raceName;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public AnimalDTO getAnimal() {
        return animal;
    }

    public void setAnimal(AnimalDTO animal) {
        this.animal = animal;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
}
