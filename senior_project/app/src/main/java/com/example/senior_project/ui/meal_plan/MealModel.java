package com.example.senior_project.ui.meal_plan;

public class MealModel {
    String mealName;
    String recipeName;

    public MealModel(String mealName, String recipeName) {
        this.mealName = mealName;
        this.recipeName = recipeName;
    }

    public String getRecipeName() {

        return recipeName;
    }

    public String getMealName() {
        return mealName;
    }
}
