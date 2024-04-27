package org.hrantlucas.endpoint;

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
import org.hrantlucas.exception.CuisineTypeNotValidException;
import org.hrantlucas.model.drink.DrinkRecipe;
import org.hrantlucas.model.meal.MealRecipe;
import org.hrantlucas.service.DrinkRecipeService;
import org.hrantlucas.service.MealRecipeService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Recipe endpoint (exposed at "recipe" path)
 */
@Path("recipe")
public class RecipeEndpoint {

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
    @Operation(summary = "Get a meal recipe by cuisine type",
            description = "Returns a meal recipe in XML format based on the specified cuisine type",
            responses = {
                    @ApiResponse(description = "The meal recipe",
                            content = @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = MealRecipe.class))),
                    @ApiResponse(responseCode = "400", description = "Cuisine type not valid"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public Response getMealByCuisineType(@Parameter(description = "Cuisine type of the desired recipe", required = true) @PathParam("cuisineType") String cuisineType) throws CuisineTypeNotValidException, JAXBException {
        // Making the call to the Meal API and getting the meal recipe
        JsonObject jsonObject = MealRecipeService.makeRequestAndGetMealAsJsonObject(client, cuisineType);
        // Getting the recipe from the json with MealRecipeService
        MealRecipe recipe = MealRecipeService.getRecipeFromJsonResponse(jsonObject);

        // Converting XMLRootElement class objet to XML string
        String xmlRecipe = convertObjectToXML(recipe);

        return Response.ok(xmlRecipe, MediaType.APPLICATION_XML)
                .build();
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
    @Operation(summary = "Get a cocktail recipe",
            description = "Returns a cocktail recipe in XML format. The recipe can be either alcoholic or non-alcoholic based on the query parameter",
            responses = {
                    @ApiResponse(description = "The drink recipe",
                            content = @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = DrinkRecipe.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public Response getCocktail(@Parameter(description = "true for alcoholic drink, false for non-alcoholic, nothing for random") @QueryParam("alcoholic") Boolean alcoholic) throws JAXBException {

        JsonObject jsonFullDrink = DrinkRecipeService.makeRequestAndGetDrinkAsJsonObject(client, alcoholic, null);
        if (jsonFullDrink == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No drinks found.").build();
        }
        DrinkRecipe drinkRecipe = DrinkRecipeService.getRecipeFromJsonResponse(jsonFullDrink);

        String xmlDrink = convertObjectToXML(drinkRecipe);

        return Response.ok(xmlDrink, MediaType.APPLICATION_XML).build();
    }

    /**
     * Method converting an object to String with XML stucture
     *
     * @param o object to convert to XML.
     * @return converted object as a String XML.
     * @throws JAXBException if there is an issue.
     */
    private String convertObjectToXML(Object o) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(o.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(o, writer);
        return writer.toString();
    }
}
