package com.example.vkrapp.utils;

public class Animal {

    private String animal_type;

    private double coefficient;
    private double weight;
    private double kkl;

    public Animal(String animal_type, double weight, double kkl, double coefficient) {
        this.animal_type = animal_type;
        this.weight = weight;
        this.kkl = kkl;
        this.coefficient = coefficient;
    }

    public double calcResult() {
        int result = 0;

        if(this.coefficient != -1) {
            double energy = 30 * this.coefficient + 70;
            double kof = (energy * 1.2);
            result = (int) Math.ceil(this.kkl * 100 / kof);
        }else {
//            другая формула для животных у который коэф в бд == -1
            result += 1;
        }

        return result;
    }
}
