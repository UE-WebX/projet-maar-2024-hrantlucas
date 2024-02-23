package org.hrantlucas;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeEndpointTest {

    private HttpServer server;
    private WebTarget target;

    @BeforeEach
    public void setUp() throws Exception {
        // start the server
        server = RecipeApplication.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(RecipeApplication.BASE_URI);
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.stop();
    }

    private void validateXmlAgainstXsd(String xmlBody, String xsdPath) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File(xsdPath));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new StringReader(xmlBody)));
    }


    @Test
    public void testGetMealByCuisineTypeSuccess() throws Exception {
        Response response = target.path("/recipe/meal/Asian").request().get();
        assertEquals(200, response.getStatus(), "the response doesn't have a 200 http status code");

        String responseBody = response.readEntity(String.class);
        assertNotNull(responseBody, "the response doesn't have a body");
        assertTrue(responseBody.contains("<cuisineType>asian</cuisineType>"), "the response doesn't contain the good cuisine type");

        validateXmlAgainstXsd(responseBody, "src/main/resources/CuisineRecipe.xsd");
    }

    @Test
    public void testGetMealByCuisineTypeInvalid() throws Exception {
        Response response = target.path("/recipe/meal/InvalidCuisine").request().get();
        assertEquals(400, response.getStatus(), "the response doesn't have a 400 http status code");

        String responseBody = response.readEntity(String.class);
        assertNotNull(responseBody, "the response doesn't have a body");
        assertTrue(responseBody.contains("<errorCode>400</errorCode>"), "the response doesn't contain the error message");

        validateXmlAgainstXsd(responseBody, "src/main/resources/CuisineRecipe.xsd");
    }

    @Test
    public void testGetMealByCuisineTypeWithServerError() throws Exception {
        Response response = target.path("/recipe/InvalidURL").request().get();
        assertEquals(500, response.getStatus(), "the response doesn't have a 500 http status code");

        String responseBody = response.readEntity(String.class);
        assertTrue(responseBody.contains("<errorCode>500</errorCode>"), "the response doesn't contain the error message");

        validateXmlAgainstXsd(responseBody, "src/main/resources/CuisineRecipe.xsd");
    }

    @Test
    public void testGetCocktailByTypeSuccess() throws Exception {
        Response response = target.path("/recipe/drink")
                .queryParam("a", "Alcoholic").request().get();
        assertEquals(200, response.getStatus(), "the response doesn't have a 200 http status code");

        String responseBody = response.readEntity(String.class);
        assertNotNull(responseBody, "the response doesn't have a body");
        assertTrue(responseBody.contains("<isAlcoholic>Alcoholic</isAlcoholic>"), "the response doesn't contain the good cocktail type");

        validateXmlAgainstXsd(responseBody, "src/main/resources/CocktailRecipe.xsd");
    }

    @Test
    public void testGetCocktailByRandomSuccess() throws Exception {
        Response response = target.path("/recipe/drink").request().get();
        assertEquals(200, response.getStatus(), "the response doesn't have a 200 http status code");

        String responseBody = response.readEntity(String.class);
        assertNotNull(responseBody, "the response doesn't have a body");

        validateXmlAgainstXsd(responseBody, "src/main/resources/CocktailRecipe.xsd");
    }

    @Test
    public void testGetCocktailWithServerError() throws Exception {
        Response response = target.path("/recipe/drink/InvalidURL").request().get();
        assertEquals(500, response.getStatus(), "the response doesn't have a 500 http status code");

        String responseBody = response.readEntity(String.class);
        assertTrue(responseBody.contains("<errorCode>500</errorCode>"), "the response doesn't contain the error message");

        validateXmlAgainstXsd(responseBody, "src/main/resources/CocktailRecipe.xsd");
    }
}
