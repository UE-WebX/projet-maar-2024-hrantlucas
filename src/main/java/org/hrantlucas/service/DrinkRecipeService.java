package org.hrantlucas.service;

import jakarta.json.JsonObject;
import org.hrantlucas.model.drink.DetailedIngredientType;
import org.hrantlucas.model.drink.DetailedType;
import org.hrantlucas.model.drink.DrinkRecipe;

import java.util.ArrayList;
import java.util.List;

public class DrinkRecipeService {


    public static DrinkRecipe getRecipeFromJsonResponse(JsonObject jsonFullDrink){
        // initializing the detailed type object
        DetailedType detailedType = new DetailedType();
        detailedType.setIsAlcoholic(jsonFullDrink.getString("strAlcoholic"));
        detailedType.setCategory(jsonFullDrink.getString("strCategory"));
        detailedType.setGlassType(jsonFullDrink.getString("strGlass"));

        // initializing ingredients object list
        StringBuilder syntheticListBuilder = new StringBuilder();
        List<DetailedIngredientType> detailedIngredients = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            String ingredient = jsonFullDrink.getString("strIngredient" + i, null);
            String measure = jsonFullDrink.getString("strMeasure" + i, null);
            if (ingredient != null && !ingredient.isEmpty()) {
                DetailedIngredientType ingredientDetail = new DetailedIngredientType();
                String completeText = measure != null ? measure + "of " + ingredient : ingredient;
                ingredientDetail.setCompletText(completeText);
                ingredientDetail.setIngredientName(ingredient);
                ingredientDetail.setIngredientQuantity(measure != null ? measure : "N/A");
                detailedIngredients.add(ingredientDetail);

                if (syntheticListBuilder.length() > 0) {
                    syntheticListBuilder.append(", ");
                }
                syntheticListBuilder.append(completeText);
            } else {
                break; // end the loop if no ingredient found
            }

        }

        // initializing the drink recipe object
        DrinkRecipe drinkRecipe = new DrinkRecipe();
        drinkRecipe.setPreparationTime("No data"); // only to validate xsd
        drinkRecipe.setSyntheticList(syntheticListBuilder.toString());
        drinkRecipe.setCocktailName(jsonFullDrink.getString("strDrink"));
        drinkRecipe.setDetailedType(detailedType);
        drinkRecipe.setImageUrl(jsonFullDrink.getString("strDrinkThumb"));
        drinkRecipe.setInstructions(jsonFullDrink.getString("strInstructions"));
        drinkRecipe.setDetailedIngredients(detailedIngredients);

        return drinkRecipe;
    }
}
