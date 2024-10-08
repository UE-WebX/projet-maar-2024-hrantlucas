package org.hrantlucas.service;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import org.hrantlucas.exception.v2.DrinkNotFoundException;
import org.hrantlucas.model.drink.DetailedIngredientType;
import org.hrantlucas.model.drink.DetailedType;
import org.hrantlucas.model.drink.DrinkRecipe;
import org.hrantlucas.model.drink.v2.DetailedIngredientTypeV2;
import org.hrantlucas.model.drink.v2.DrinkRecipeV2;
import org.hrantlucas.model.menu.MenuFilter;
import org.hrantlucas.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DrinkRecipeService {

    /**
     * Method building DrinkRecipe object based on a JsonObject object.
     *
     * @param jsonFullDrink drink recipe as a JsonObject object.
     * @return DrinkRecipe object
     */
    public static DrinkRecipe getRecipeFromJsonResponse(JsonObject jsonFullDrink) {
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
                String completeText = measure != null ? ingredient + " - " + measure : ingredient;
                ingredientDetail.setCompletText(completeText);
                ingredientDetail.setIngredientName(ingredient);
                ingredientDetail.setIngredientQuantity(measure != null ? measure : "N/A");
                detailedIngredients.add(ingredientDetail);

                if (syntheticListBuilder.length() > 0) syntheticListBuilder.append(", ");
                syntheticListBuilder.append(completeText);
            } else {
                break; // end the loop if no ingredient found
            }

        }

        // initializing the drink recipe object
        DrinkRecipe drinkRecipe = new DrinkRecipe();
        drinkRecipe.setSyntheticList(syntheticListBuilder.toString());
        drinkRecipe.setCocktailName(jsonFullDrink.getString("strDrink"));
        drinkRecipe.setDetailedType(detailedType);
        drinkRecipe.setImageUrl(jsonFullDrink.getString("strDrinkThumb"));
        drinkRecipe.setInstructions(jsonFullDrink.getString("strInstructions"));
        drinkRecipe.setDetailedIngredients(detailedIngredients);

        return drinkRecipe;
    }

    public static DrinkRecipeV2 getRecipeFromJsonResponseV2(JsonObject jsonFullDrink) {

        DrinkRecipeV2 drinkRecipeV2 = new DrinkRecipeV2();
        drinkRecipeV2.setName(jsonFullDrink.getString("strDrink"));
        drinkRecipeV2.setType(jsonFullDrink.getString("strCategory"));
        drinkRecipeV2.setAlcoholic(Objects.equals(jsonFullDrink.getString("strAlcoholic"), "Alcoholic"));
        drinkRecipeV2.setImageURL(jsonFullDrink.getString("strDrinkThumb"));
        drinkRecipeV2.setInstructions(jsonFullDrink.getString("strInstructions"));

        List<String> ingredients = new ArrayList<>();
        List<DetailedIngredientTypeV2> detailedIngredients = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            String ingredient = jsonFullDrink.getString("strIngredient" + i, "");
            String measure = jsonFullDrink.getString("strMeasure" + i, "");
            if (ingredient != null && !ingredient.trim().isEmpty()) {
                DetailedIngredientTypeV2 detailedIngredient = new DetailedIngredientTypeV2();
                detailedIngredient.setIngredientName(ingredient);
                detailedIngredient.setIngredientQuantity(measure);
                detailedIngredient.setImage("N/A");
                detailedIngredients.add(detailedIngredient);

                String completeText = measure != null ? ingredient + " - " + measure : ingredient;
                ingredients.add(completeText);
            } else {
                break; // end the loop if no ingredient found
            }
        }
        drinkRecipeV2.setIngredients(ingredients);
        drinkRecipeV2.setDetailedIngredients(detailedIngredients);
        drinkRecipeV2.setSuccess(true);

        return drinkRecipeV2;
    }

    public static JsonObject makeRequestAndGetDrinkAsJsonObject(Client client, Boolean alcoholic, MenuFilter menuFilter) throws DrinkNotFoundException {
        if (alcoholic == null) {
            alcoholic = new Random().nextBoolean();
        }

        JsonArray drinks;
        WebTarget target = client.target(Constants.DRINK_EXTERNAL_URI)
                .path("/api/json/v1/{apiKey}/filter.php")
                .resolveTemplate("apiKey", Constants.DRINK_APPLICATION_KEY);

        if (menuFilter != null) {
            if (menuFilter.getPresentIngredient() != null) {
                target = target.queryParam("i", menuFilter.getPresentIngredient());
            }
            if (menuFilter.getAlcoholic() != null) {
                target = target.queryParam("a", menuFilter.getAlcoholic() ? "Alcoholic" : "Non_Alcoholic");
            }
        }

        if (menuFilter == null || menuFilter.getPresentIngredient() == null && menuFilter.getAlcoholic() == null) {
            target = target.queryParam("a", alcoholic ? "Alcoholic" : "Non_Alcoholic");
        }

        String randomDrinkId = "1";

        try {
            drinks = target.request(MediaType.APPLICATION_JSON)
                    .get(JsonObject.class)
                    .getJsonArray("drinks");

            if (drinks == null || drinks.isEmpty()) {
                return null;
            }

            randomDrinkId = drinks.getJsonObject(new Random().nextInt(drinks.size())).getString("idDrink");
        } catch (Exception e) {
            throw new DrinkNotFoundException("API POST /menu");
        }

        return client.target(Constants.DRINK_EXTERNAL_URI)
                .path("/api/json/v1/{apiKey}/lookup.php")
                .resolveTemplate("apiKey", Constants.DRINK_APPLICATION_KEY)
                .queryParam("i", randomDrinkId)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class)
                .getJsonArray("drinks")
                .getJsonObject(0);
    }
}
