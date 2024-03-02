package org.hrantlucas.service;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.hrantlucas.model.meal.Ingredient;
import org.hrantlucas.model.meal.MealRecipe;
import org.hrantlucas.model.meal.v2.IngredientV2;
import org.hrantlucas.model.meal.v2.MealRecipeV2;

import java.util.ArrayList;
import java.util.List;

public class MealRecipeService {

    /**
     * Method building MealRecipe object based on a JsonObject object.
     *
     * @param jsonObject drink recipe as a JsonObject object.
     * @return MealRecipe object
     */
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

    public static MealRecipeV2 getRecipeFromJsonResponseV2(JsonObject jsonObject) {
        MealRecipeV2 recipeV2 = new MealRecipeV2();

        recipeV2.setName(jsonObject.get("label").toString().replaceAll("\"", ""));

        recipeV2.setType(jsonObject.get("mealType").asJsonArray().get(0).toString().replaceAll("\"", ""));

        recipeV2.setCountry(jsonObject.get("cuisineType").asJsonArray().get(0).toString().replaceAll("\"", ""));

        recipeV2.setPreparationTime(jsonObject.get("totalTime").toString().replaceAll("\"", ""));

        recipeV2.setImageURL(jsonObject.get("image").toString().replaceAll("\"", ""));

        recipeV2.setSource(jsonObject.get("url").toString().replaceAll("\"", ""));

        StringBuilder ingredients = new StringBuilder();
        IngredientV2 ingredientV2;
        List<IngredientV2> listIngredientV2 = new ArrayList<>();
        for (JsonValue i : jsonObject.get("ingredients").asJsonArray()) {
            ingredients.append(i.asJsonObject().get("food").toString().replaceAll("\"", ""));
            ingredients.append(",");

            ingredientV2 = new IngredientV2();

            ingredientV2.setName(i.asJsonObject().get("text").toString().replaceAll("\"", ""));
            ingredientV2.setQuantity(i.asJsonObject().get("quantity").toString().replaceAll("\"", ""));
            ingredientV2.setImage(i.asJsonObject().get("image").toString().replaceAll("\"", ""));
            listIngredientV2.add(ingredientV2);
        }
        ingredients.deleteCharAt(ingredients.length() - 1);

        recipeV2.setIngredients(ingredients.toString());

        recipeV2.setDetailedIngredients(listIngredientV2);

        JsonValue instructionsJson = jsonObject.get("instructions");
        if (instructionsJson == null) {
            List<String> defaultInstructions = new ArrayList<>();
            defaultInstructions.add("Mix the ingredients altogether");
            recipeV2.setInstructions(defaultInstructions);
        } else {
            List<String> instructions = new ArrayList<>();
            for (JsonValue i : instructionsJson.asJsonArray()) {
                instructions.add(i.toString());
            }
            recipeV2.setInstructions(instructions);
        }
        recipeV2.setSuccess(true);

        return recipeV2;
    }
}
