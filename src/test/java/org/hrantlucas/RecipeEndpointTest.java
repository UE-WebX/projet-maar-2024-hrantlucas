package org.hrantlucas;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(RecipeApplication.BASE_URI);
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testGetRecipeByCuisineTypeSuccess() {
        Response response = target.path("/recipe/meal/Asian").request().get();
        assertEquals( 200, response.getStatus(), "the response doesn't have a 200 http status code");

        String responseBody = response.readEntity(String.class);
        assertNotNull(responseBody, "the response doesn't have a body");
        assertTrue(responseBody.contains("<cuisineType>asian</cuisineType>"), "the response doesn't contain the good cuisine type");
    }

    @Test
    public void testGetRecipeByCuisineTypeInvalid() {
        Response response = target.path("/recipe/meal/InvalidCuisine").request().get();
        assertEquals(400, response.getStatus(), "the response doesn't have a 400 http status code");

        String responseBody = response.readEntity(String.class);
        assertNotNull(responseBody, "the response doesn't have a body");
        assertTrue(responseBody.contains("<errorCode>400</errorCode>"), "the response doesn't contain the error message");
    }

    @Test
    public void testGetRecipeByCuisineTypeWithServerError() {
        Response response = target.path("/recipe/InvalidURL").request().get();
        assertEquals(500, response.getStatus(), "the response doesn't have a 500 http status code");

        String responseBody = response.readEntity(String.class);
        assertTrue(responseBody.contains("<errorCode>500</errorCode>"), "the response doesn't contain the error message");
    }
}
