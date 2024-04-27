package org.hrantlucas.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

/**
 * Recipe V2 endpoint (exposed at "v2/recipe" path)
 */
@Path("v2/recipe")
public class RecipeV2Endpoint {

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
    @Operation(summary = "Get a V2 meal recipe by cuisine type",
            description = "Returns a V2 meal recipe in JSON format based on the specified cuisine type",
            responses = {
                    @ApiResponse(description = "The V2 meal recipe",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MealRecipeV2.class))),
                    @ApiResponse(responseCode = "400", description = "Cuisine type not valid"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public Response getMealByCuisineTypeV2(@Parameter(description = "Cuisine type of the desired recipe") @PathParam("cuisineType") String cuisineType) throws CuisineTypeNotValidV2Exception {
        // Making the call to the Meal API and getting the meal recipe
        JsonObject jsonObject = MealRecipeService.makeRequestAndGetMealV2AsJsonObject(client, cuisineType, null, "API GET /v2/recipe/meal", null);
        // Getting the recipe from the json with MealRecipeService
        MealRecipeV2 mealRecipeV2 = MealRecipeService.getRecipeFromJsonResponseV2(jsonObject);

        return Response.ok(mealRecipeV2, MediaType.APPLICATION_JSON).build();
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
    @Operation(summary = "Get a V2 cocktail recipe",
            description = "Returns a V2 cocktail recipe in JSON format. The recipe can be alcoholic or non-alcoholic depending on the query parameter",
            responses = {
                    @ApiResponse(description = "The V2 drink recipe",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DrinkRecipeV2.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })

    public Response getCocktail(@Parameter(description = "true for alcoholic drink, false for non-alcoholic, nothing for random") @QueryParam("alcoholic") Boolean alcoholic) throws JsonProcessingException {

        JsonObject jsonFullDrink = DrinkRecipeService.makeRequestAndGetDrinkAsJsonObject(client, alcoholic, null);
        if (jsonFullDrink == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No drinks found.").build();
        }
        DrinkRecipeV2 drinkRecipe = DrinkRecipeService.getRecipeFromJsonResponseV2(jsonFullDrink);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(drinkRecipe);

        return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
    }
}
