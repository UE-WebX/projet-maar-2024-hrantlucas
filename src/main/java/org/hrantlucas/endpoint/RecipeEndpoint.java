package org.hrantlucas.endpoint;

import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hrantlucas.exception.CuisineTypeNotValidException;
import org.json.JSONObject;
import org.json.XML;

import java.util.Random;

/**
 * Recipe endpoint (exposed at "recipe" path)
 */
@Path("recipe")
public class RecipeEndpoint {

    private static final String APPLICATION_ID = "8528fa1e";
    private static final String APPLICATION_KEY = "c22b964fb40477e835dfb7087026ce89";
    private static final String EXTERNAL_URI = "https://api.edamam.com/";
    private Client client = ClientBuilder.newClient();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/xml" media type.
     *
     * @param cuisineType cuisine type of the desired recipe.
     * @return recipe that will be returned as an "application/xml" response.
     * @throws CuisineTypeNotValidException if the cuisine type is invalid or unknown
     */
    @GET
    @Path("/meal/{cuisineType}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getRecipeByCuisineType(@PathParam("cuisineType") String cuisineType) throws CuisineTypeNotValidException {
        // get the response by the cuisine type
        JsonObject jsonResponse = client.target(EXTERNAL_URI)
                .path("api/recipes/v2/")
                .queryParam("type", "public")
                .queryParam("app_id", APPLICATION_ID)
                .queryParam("app_key", APPLICATION_KEY)
                .queryParam("cuisineType", cuisineType)
                .queryParam("field", "cuisineType")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);
        System.out.println(jsonResponse);
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

        // Converting JSON to XML
        JSONObject tempJsonObject = new JSONObject(jsonFullRecipe.toString());
        String xmlRecipe = "<recipe>" + XML.toString(tempJsonObject) + "</recipe>";

        return Response.ok(xmlRecipe, MediaType.APPLICATION_XML)
                .build();
    }
}
