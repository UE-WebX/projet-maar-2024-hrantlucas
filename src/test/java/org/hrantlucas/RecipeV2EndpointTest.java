package org.hrantlucas;

import jakarta.json.JsonReader;
import jakarta.json.stream.JsonParsingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.*;

public class RecipeV2EndpointTest {

    private HttpServer server;
    private WebTarget target;

    private static final String MEAL_SCHEMA_JSON = "src/main/resources/meal_schema.json";

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

    private void validateJsonAgainstSchema(String jsonBody) throws Exception {
        JsonValidationService service = JsonValidationService.newInstance();
        JsonSchema jsonSchema = null;

        try (InputStream schemaStream = Files.newInputStream(Paths.get(MEAL_SCHEMA_JSON))) {
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
    public void testGetMealByCuisineTypeSuccess() throws Exception {
        Response response = target.path("/v2/recipe/meal/Asian").request().get();
        assertEquals(200, response.getStatus(), "the response doesn't have a 200 http status code");

        String responseBody = response.readEntity(String.class);
        assertNotNull(responseBody, "the response doesn't have a body");
        assertTrue(responseBody.contains("\"country\":\"asian\""), "the response doesn't contain the good country");

        validateJsonAgainstSchema(responseBody);
    }

    @Test
    public void testGetMealByCuisineTypeInvalid() throws Exception {
        Response response = target.path("/v2/recipe/meal/InvalidCuisine").request().get();
        assertEquals(400, response.getStatus(), "the response doesn't have a 400 http status code");

        String responseBody = response.readEntity(String.class);
        assertNotNull(responseBody, "the response doesn't have a body");
        assertTrue(responseBody.contains("\"api_status\": \"400\""), "the response doesn't contain the error code");

        validateJsonAgainstSchema(responseBody);
    }

    @Test
    public void testGetMealByCuisineTypeWithServerError() throws Exception {
        Response response = target.path("/v2/recipe/InvalidURL").request().get();
        assertEquals(500, response.getStatus(), "the response doesn't have a 500 http status code");

        String responseBody = response.readEntity(String.class);
        assertTrue(responseBody.contains("\"api_status\": \"500\""), "the response doesn't contain the error code");

        validateJsonAgainstSchema(responseBody);
    }
}
