package org.hrantlucas.endpoint;

import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hrantlucas.exception.v2.CuisineTypeNotValidV2Exception;
import org.hrantlucas.model.drink.v2.DrinkRecipeV2;
import org.hrantlucas.model.meal.v2.MealRecipeV2;
import org.hrantlucas.model.menu.Menu;
import org.hrantlucas.model.menu.MenuFilter;
import org.hrantlucas.service.DrinkRecipeService;
import org.hrantlucas.service.MealRecipeService;
import org.hrantlucas.utils.Constants;

/**
 * Menu endpoint (exposed at "/menu" path)
 */
@Path("menu")
public class MenuEndpoint {

    private Client client = ClientBuilder.newClient();

    /**
     * Method handling HTTP POST requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @param menuFilter filter based on which the menu will be created.
     * @return menu that will be returned as an "application/json" response.
     * @throws CuisineTypeNotValidV2Exception if the cuisine type in the filter is invalid or unknown
     */
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMenu(MenuFilter menuFilter) throws CuisineTypeNotValidV2Exception {
        Menu menu = new Menu();

        // STARTER
        // Making the call to the Meal API and getting the meal recipe
        JsonObject jsonObject = MealRecipeService.makeRequestAndGetMealV2AsJsonObject(client, menuFilter.getCuisineType(), Constants.MEAL_STARTER, "API POST /menu", menuFilter);
        // Getting the recipe from the json with MealRecipeService
        MealRecipeV2 starterRecipe = MealRecipeService.getRecipeFromJsonResponseV2(jsonObject);
        menu.setStarter(starterRecipe);

        // MAIN COURSE
        jsonObject = MealRecipeService.makeRequestAndGetMealV2AsJsonObject(client, menuFilter.getCuisineType(), Constants.MEAL_MAIN_COURSE, "API POST /menu", menuFilter);
        MealRecipeV2 mainCourseRecipe = MealRecipeService.getRecipeFromJsonResponseV2(jsonObject);
        menu.setMainCourse(mainCourseRecipe);

        // DESSERT
        jsonObject = MealRecipeService.makeRequestAndGetMealV2AsJsonObject(client, menuFilter.getCuisineType(), Constants.MEAL_DESSERT, "API POST /menu", menuFilter);
        MealRecipeV2 dessertRecipe = MealRecipeService.getRecipeFromJsonResponseV2(jsonObject);
        menu.setDessert(dessertRecipe);

        // DRINK
        JsonObject jsonFullDrink = DrinkRecipeService.makeRequestAndGetDrinkAsJsonObject(client, null, menuFilter);
        if (jsonFullDrink == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No drinks found.").build();
        }
        DrinkRecipeV2 drinkRecipe = DrinkRecipeService.getRecipeFromJsonResponseV2(jsonFullDrink);
        menu.setDrink(drinkRecipe);

        return Response.ok(menu, MediaType.APPLICATION_JSON).build();
    }
}
