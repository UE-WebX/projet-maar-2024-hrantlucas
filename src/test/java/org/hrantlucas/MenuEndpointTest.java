package org.hrantlucas;

import jakarta.json.JsonReader;
import jakarta.json.stream.JsonParsingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.Problem;
import org.leadpony.justify.api.ProblemHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuEndpointTest {

    private HttpServer server;
    private WebTarget target;
    private static final String MENU_SCHEMA_JSON = "src/main/resources/menu_schema.json";

    @BeforeEach
    public void setUp() throws Exception {
        server = RecipeApplication.startServer();
        Client client = ClientBuilder.newClient();
        target = client.target(RecipeApplication.BASE_URI);
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.stop();
    }

    private void validateJsonAgainstSchema(String jsonBody, String jsonSchemaPath) throws Exception {
        JsonValidationService service = JsonValidationService.newInstance();
        JsonSchema jsonSchema = null;

        try (InputStream schemaStream = Files.newInputStream(Paths.get(jsonSchemaPath))) {
            jsonSchema = service.readSchema(schemaStream);
        } catch (Exception e) {
            System.err.println("Failed to read the JSON schema: " + e);
        }
        List<Problem> problems = new ArrayList<>();
        ProblemHandler problemHandler = problems::addAll;
        InputStream jsonStream = new ByteArrayInputStream(jsonBody.getBytes(StandardCharsets.UTF_8));
        try (JsonReader reader = service.createReader(jsonStream, jsonSchema, problemHandler)) {
            reader.readValue();
            if (!problems.isEmpty()) {
                for (Problem problem : problems) {
                    System.out.println(problem);
                }
            }
            assertTrue(problems.isEmpty());
        } catch (JsonParsingException e) {
            System.err.println("Failed to validate the JSON response: " + e);
        } catch (Exception e) {
            System.err.println("Failed to parse the JSON response: " + e);
        }
    }

    @Test
    public void testPostMenuEmptySuccess() throws Exception {
        String requestJson = "{\"cuisineType\": \"Asian\"}";
        Response response = target.path("/menu")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(requestJson, MediaType.APPLICATION_JSON));

        Assertions.assertEquals(200, response.getStatus(), "Expected HTTP status 200");
        String responseBody = response.readEntity(String.class);
        Assertions.assertNotNull(responseBody, "Response body should not be null");

        validateJsonAgainstSchema(responseBody, MENU_SCHEMA_JSON);
    }

    @Test
    public void testPostMenuFullSuccess() throws Exception {
        String requestJson = "{\"cuisineType\": \"Asian\", \"alcoholic\": false, \"presentIngredient\": \"water\", \"maxPreparationTime\": \"0\", \"constraints\": [\"pork-free\"]}";
        Response response = target.path("/menu")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(requestJson, MediaType.APPLICATION_JSON));

        Assertions.assertEquals(200, response.getStatus(), "Expected HTTP status 200");
        String responseBody = response.readEntity(String.class);
        Assertions.assertNotNull(responseBody, "Response body should not be null");

        validateJsonAgainstSchema(responseBody, MENU_SCHEMA_JSON);

        Assertions.assertTrue(responseBody.contains("\"country\":\"asian\""), "Response should reflect the requested cuisine type: Asian.");
        Assertions.assertTrue(responseBody.contains("\"alcoholic\":false"), "Response should indicate that the drink is non-alcoholic.");
        Assertions.assertTrue(responseBody.contains("water"), "Response should include 'water' as an ingredient.");
        Assertions.assertFalse(responseBody.contains("pork"), "Response should reflect pork-free constraints.");
        Assertions.assertTrue(responseBody.contains("\"preparationTime\":\"0.0\""), "Response should respect the maximum preparation time of 0 minutes.");
    }

    @Test
    public void testPostMenuInvalidCuisineType() throws Exception {
        String requestJson = "{\"cuisineType\": \"Invalid\"}";
        Response response = target.path("/menu")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(requestJson, MediaType.APPLICATION_JSON));

        Assertions.assertEquals(400, response.getStatus(), "Expected HTTP status 400");
        String responseBody = response.readEntity(String.class);
        Assertions.assertTrue(responseBody.contains("failed"), "Expected error message in response body");
    }

    @Test
    public void testPostMenuServerError() throws Exception {
        String requestJson = "{\"cuisineType\": \"\"}";
        Response response = target.path("/menu")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(requestJson, MediaType.APPLICATION_JSON));

        Assertions.assertEquals(400, response.getStatus(), "Expected HTTP status 400");
        String responseBody = response.readEntity(String.class);
        Assertions.assertTrue(responseBody.contains("error"), "Expected error message in response body");
    }
}
