package com.example.vkrapp;

import java.util.HashMap;

public class CalcLogic {

    private static HashMap<String, Double> cats_coefficient = new HashMap<String, Double>() {
        {
            put("Взрослый", 0.1);
            put("Активный взрослый", 0.2);
            put("Снижение веса", 0.3);
            put("Болеет", 0.4);
            put("Беременность", 0.5);
            put("Возраст 4-12 месяцев", 0.6);
            put("Возраст > 7 лет", 0.7);
        }
    };

    public static int getCoefficient(Animal animal) {

        switch (animal.getAnimalType().toLowerCase()) {
            case "собака":
            case "попугай":
            case "хомяк":
            case "крыса":
            case "кошка": return getCatsCoefficient(animal);

            default: return 0;
        }
    }

    private static int getCatsCoefficient(Animal animal) {
//        Любая логика с учетом параметров объекта Animal (все что вернет - юзаем как коэфы для рассчетов)
//        КОЭФЫ ЗАЛОЖЕННЫЕ В ЗАВИСИМОСТИ ОТ ТИПА ПИТОМЦА
        double life_coefficient = cats_coefficient.get(animal.getLifeStage());

//        ФОРМУЛА ДЛЯ РАССЧЕТА ГР.
        double energy = 30 * life_coefficient + 70;
        double kof = (energy * 1.2);
        int result = (int) Math.ceil(animal.getKkl() * 100/ kof);

        return result;
    }
}
