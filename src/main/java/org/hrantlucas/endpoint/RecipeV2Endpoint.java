package org.hrantlucas.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hrantlucas.exception.v2.CuisineTypeNotValidV2Exception;
import org.hrantlucas.model.drink.v2.DrinkRecipeV2;
import org.hrantlucas.model.meal.v2.MealRecipeV2;
import org.hrantlucas.service.DrinkRecipeService;
import org.hrantlucas.service.MealRecipeService;

import java.util.Random;

/**
 * Recipe V2 endpoint (exposed at "v2/recipe" path)
 */
@Path("v2/recipe")
public class RecipeV2Endpoint {

    private static final String MEAL_APPLICATION_ID = "8528fa1e";
    private static final String MEAL_APPLICATION_KEY = "c22b964fb40477e835dfb7087026ce89";
    private static final String MEAL_EXTERNAL_URI = "https://api.edamam.com/";

    private static final String DRINK_APPLICATION_KEY = "1";
    private static final String DRINK_EXTERNAL_URI = "https://www.thecocktaildb.com/";
    private Client client = ClientBuilder.newClient();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @param cuisineType cuisine type of the desired recipe.
     * @return meal recipe that will be returned as an "application/json" response.
     * @throws CuisineTypeNotValidV2Exception if the cuisine type is invalid or unknown
     */
    @GET
    @Path("/meal/{cuisineType}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMealByCuisineTypeV2(@PathParam("cuisineType") String cuisineType) throws CuisineTypeNotValidV2Exception {
        // get the response by the cuisine type
        JsonObject jsonResponse = client.target(MEAL_EXTERNAL_URI)
                .path("api/recipes/v2/")
                .queryParam("type", "public")
                .queryParam("app_id", MEAL_APPLICATION_ID)
                .queryParam("app_key", MEAL_APPLICATION_KEY)
                .queryParam("cuisineType", cuisineType)
                .queryParam("field", "cuisineType")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);
        // number of recipes in the response
        int count = Integer.parseInt(jsonResponse.get("to").toString());

        // if no recipes found, throw an exception
        if (count == 0) {
            throw new CuisineTypeNotValidV2Exception("API GET /v2/recipe/meal");
        }

        // Finding necessary part of the response
        JsonObject jsonRecipe = jsonResponse.get("hits").asJsonArray()
                .get(new Random().nextInt(count)).asJsonObject();
        String fullRecipeUrl = jsonRecipe.get("_links").asJsonObject()
                .get("self").asJsonObject()
                .get("href").toString();

        JsonObject jsonFullRecipe = client.target(fullRecipeUrl.substring(1, fullRecipeUrl.length() - 1))
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class).get("recipe").asJsonObject();

        // Getting storing the recipe from the json with RecipeService
        MealRecipeV2 recipeV2 = MealRecipeService.getRecipeFromJsonResponseV2(jsonFullRecipe);

        return Response.ok(recipeV2, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @param alcoholic true for alcoholic drink, false for non-alcoholic, nothing for random.
     * @return drink recipe that will be returned as an "application/json" response.
     */
    @GET
    @Path("/drink")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCocktail(@QueryParam("alcoholic") Boolean alcoholic) throws JsonProcessingException {
        if (alcoholic == null) {
            alcoholic = new Random().nextBoolean();
        }

        JsonArray drinks = client.target(DRINK_EXTERNAL_URI)
                .path("/api/json/v1/{apiKey}/filter.php")
                .resolveTemplate("apiKey", DRINK_APPLICATION_KEY)
                .queryParam("a", alcoholic ? "Alcoholic" : "Non_Alcoholic")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class)
                .getJsonArray("drinks");

        if (drinks == null || drinks.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No drinks found.").build();
        }

        String randomDrinkId = drinks.getJsonObject(new Random().nextInt(drinks.size())).getString("idDrink");

        JsonObject jsonFullDrink = client.target(DRINK_EXTERNAL_URI)
                .path("/api/json/v1/{apiKey}/lookup.php")
                .resolveTemplate("apiKey", DRINK_APPLICATION_KEY)
                .queryParam("i", randomDrinkId)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class)
                .getJsonArray("drinks")
                .getJsonObject(0);

        DrinkRecipeV2 drinkRecipe = DrinkRecipeService.getRecipeFromJsonResponseV2(jsonFullDrink);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(drinkRecipe);

        return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
    }
}
