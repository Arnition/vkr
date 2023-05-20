package com.example.vkrapp;

public class Animal {

    private String animal_type;
    private String life_stage;
    private double weight;
    private double kkl;

    public Animal(String animal_type, String life_stage, double weight, double kkl) {
        this.animal_type = animal_type;
        this.life_stage = life_stage;
        this.weight = weight;
        this.kkl = kkl;
    }

    public String getLifeStage() {
        return life_stage;
    }

    public double getWeight() {
        return weight;
    }

    public String getAnimalType() {
        return animal_type;
    }

    public double getKkl() {
        return kkl;
    }
}
