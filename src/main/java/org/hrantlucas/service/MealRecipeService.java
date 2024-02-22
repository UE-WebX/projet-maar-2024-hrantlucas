package org.hrantlucas.service;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.hrantlucas.model.meal.Ingredient;
import org.hrantlucas.model.meal.MealRecipe;

import java.util.ArrayList;
import java.util.List;

public class MealRecipeService {
    public static MealRecipe getRecipeFromJsonResponse(JsonObject jsonObject) {
        MealRecipe recipe = new MealRecipe();

        recipe.setLabel(jsonObject.get("label").toString().replaceAll("\"", ""));

        List<String> temp = new ArrayList<>();
        for (JsonValue mealType : jsonObject.get("mealType").asJsonArray()) {
            temp.add(mealType.toString().replaceAll("\"", ""));
        }
        recipe.setMealTypes(temp);

        temp = new ArrayList<>();
        for (JsonValue dishType : jsonObject.get("dishType").asJsonArray()) {
            temp.add(dishType.toString().replaceAll("\"", ""));
        }
        recipe.setDishTypes(temp);

        temp = new ArrayList<>();
        for (JsonValue cuisineType : jsonObject.get("cuisineType").asJsonArray()) {
            temp.add(cuisineType.toString().replaceAll("\"", ""));
        }
        recipe.setCuisineTypes(temp);

        recipe.setTotalTime(jsonObject.get("totalTime").toString().replaceAll("\"", ""));

        recipe.setImage(jsonObject.get("image").toString().replaceAll("\"", ""));

        temp = new ArrayList<>();
        for (JsonValue ingredientLine : jsonObject.get("ingredientLines").asJsonArray()) {
            temp.add(ingredientLine.toString().replaceAll("\"", ""));
        }
        recipe.setIngredientLines(temp);

        recipe.setUrl(jsonObject.get("url").toString().replaceAll("\"", ""));

        Ingredient ingredient = new Ingredient();
        List<Ingredient> ingredients = new ArrayList<>();
        for (JsonValue i : jsonObject.get("ingredients").asJsonArray()) {
            ingredient.setText(i.asJsonObject().get("text").toString().replaceAll("\"", ""));
            ingredient.setFood(i.asJsonObject().get("food").toString().replaceAll("\"", ""));
            ingredient.setQuantity(i.asJsonObject().get("quantity").toString().replaceAll("\"", ""));
            ingredient.setImage(i.asJsonObject().get("image").toString().replaceAll("\"", ""));
            ingredients.add(ingredient);
        }

        recipe.setIngredients(ingredients);

        temp = new ArrayList<>();
        for (JsonValue caution : jsonObject.get("cautions").asJsonArray()) {
            temp.add(caution.toString().replaceAll("\"", ""));
        }
        recipe.setCautions(temp);

        recipe.setCalories(jsonObject.get("calories").toString().replaceAll("\"", ""));

        return recipe;
    }
}
