package org.hrantlucas.model.menu;

import org.hrantlucas.model.drink.v2.DrinkRecipeV2;
import org.hrantlucas.model.meal.v2.MealRecipeV2;

public class Menu {
    private MealRecipeV2 starter;
    private MealRecipeV2 mainCourse;
    private MealRecipeV2 dessert;
    private DrinkRecipeV2 drink;

    public MealRecipeV2 getStarter() {
        return starter;
    }

    public void setStarter(MealRecipeV2 starter) {
        this.starter = starter;
    }

    public MealRecipeV2 getMainCourse() {
        return mainCourse;
    }

    public void setMainCourse(MealRecipeV2 mainCourse) {
        this.mainCourse = mainCourse;
    }

    public MealRecipeV2 getDessert() {
        return dessert;
    }

    public void setDessert(MealRecipeV2 dessert) {
        this.dessert = dessert;
    }

    public DrinkRecipeV2 getDrink() {
        return drink;
    }

    public void setDrink(DrinkRecipeV2 drink) {
        this.drink = drink;
    }
}
