package com.example.lykia.roommate.DTOs;

public class AnimalDTO {

    private int animalId;
    private String animalName;

    public AnimalDTO() {
    }

    public AnimalDTO(String animalName) {
        this.animalName = animalName;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }
}
