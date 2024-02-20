package org.hrantlucas.endpoint;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hrantlucas.exception.CuisineTypeNotValidException;
import org.hrantlucas.model.drink.DetailedIngredientType;
import org.hrantlucas.model.drink.DetailedType;
import org.hrantlucas.model.drink.DrinkRecipe;
import org.hrantlucas.model.meal.MealRecipe;
import org.hrantlucas.service.RecipeService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Recipe endpoint (exposed at "recipe" path)
 */
@Path("recipe")
public class RecipeEndpoint {

    private static final String MEAL_APPLICATION_ID = "8528fa1e";
    private static final String MEAL_APPLICATION_KEY = "c22b964fb40477e835dfb7087026ce89";
    private static final String MEAL_EXTERNAL_URI = "https://api.edamam.com/";

    private static final String DRINK_APPLICATION_KEY = "1";
    private static final String DRINK_EXTERNAL_URI = "https://www.thecocktaildb.com/";
    private Client client = ClientBuilder.newClient();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/xml" media type.
     *
     * @param cuisineType cuisine type of the desired recipe.
     * @return meal recipe that will be returned as an "application/xml" response.
     * @throws CuisineTypeNotValidException if the cuisine type is invalid or unknown
     */
    @GET
    @Path("/meal/{cuisineType}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getMealByCuisineType(@PathParam("cuisineType") String cuisineType) throws CuisineTypeNotValidException, JAXBException {
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
            throw new CuisineTypeNotValidException(cuisineType);
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
        MealRecipe recipe = RecipeService.getRecipeFromJsonResponse(jsonFullRecipe);

        // Converting XMLRootElement class objet to XML string
        String xmlRecipe = convertObjectToXML(recipe);

        return Response.ok(xmlRecipe, MediaType.APPLICATION_XML)
                .build();
    }

    // convert an object to a XML string
    public String convertObjectToXML(Object o) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(o.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(o, writer);
        return writer.toString();
    }


    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/xml" media type.
     *
     * @param alcoholic true for alcoholic drink, false for non-alcoholic, nothing for random.
     * @return drink recipe that will be returned as an "application/xml" response.
     */
    @GET
    @Path("/drink")
    @Produces(MediaType.APPLICATION_XML)
    public Response getCocktail(@QueryParam("alcoholic") Boolean alcoholic) throws JAXBException {

        JsonObject jsonResponse;

        // if not specified choose randomly between alcoholic and non-alcoholic cocktail
        if(alcoholic == null) alcoholic = new Random().nextBoolean();


        // get the response of the drink list with alcoholic parameter
        JsonArray drinks = client.target(DRINK_EXTERNAL_URI)
                .path("/api/json/v1/1/filter.php")
                .queryParam("a", alcoholic ? "Alcoholic" : "Non_Alcoholic")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class)
                .getJsonArray("drinks");

        // get a random drink id from the received list
        String randomDrinkId = drinks.getJsonObject(new Random().
                        nextInt(drinks.size()))
                .getString("idDrink");

        // get full details of the random drink
        JsonObject jsonFullDrink = client.target(DRINK_EXTERNAL_URI)
                .path("/api/json/v1/{apiKey}/lookup.php")
                .resolveTemplate("apiKey", DRINK_APPLICATION_KEY)
                .queryParam("i", randomDrinkId)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class)
                .getJsonArray("drinks")
                .getJsonObject(0);


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

        String xmlDrink = convertObjectToXML(drinkRecipe);

        return Response.ok(xmlDrink, MediaType.APPLICATION_XML).build();
    }
}
