package com.example.vkrapp.utils;

public class Animal {

    private String animal_type;
    private String recommendation;

    private double coefficient;
    private double defaultAnimalCoefficient;
    private double weight;
    private double grams;
    private double kkl;
    private double result;

    private boolean hasAnimalParams;

    public Animal(String animal_type, String recommendation, double defaultAnimalCoefficient, double weight, double grams, double kkl, double coefficient, boolean hasAnimalParams) {
        this.animal_type = animal_type;
        this.recommendation = recommendation;
        this.weight = weight;
        this.grams = grams;
        this.kkl = kkl;
        this.defaultAnimalCoefficient = defaultAnimalCoefficient;
        this.coefficient = coefficient;
        this.hasAnimalParams = hasAnimalParams;
    }

    public String getAnimalType() {
        return animal_type;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public double calcResult() {
//        для декор кроликов и хомяка, крысы
        if(defaultAnimalCoefficient > 0) {
            return weight * defaultAnimalCoefficient;
        }


//для кроликов
        if(weight == 0 && kkl == 0 && coefficient == 0 && hasAnimalParams) {
            return grams;
        }

//      попугаии и свинки
        if(weight > 0 && kkl == 0 && coefficient == 0 && hasAnimalParams) {
            return ((weight * grams) / 100);
        }

////      крысы
//        if(weight > 0  && kkl == 0 && coefficient == 0 && !hasAnimalParams) {
//            return ((weight * grams) / 100);
//        }
        //для собак и кошек
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

//
////        декор Кролики
//        if(weight > 0 && kkl == 0 && coefficient == 0 && !hasAnimalParams) {
//            return weight * 40;
//        }