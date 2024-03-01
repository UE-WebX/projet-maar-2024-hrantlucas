package org.hrantlucas.exception.v2;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CuisineTypeNotValidV2Exception extends Exception implements ExceptionMapper<CuisineTypeNotValidV2Exception> {


    public CuisineTypeNotValidV2Exception() {
        super("The request was failed, please try again");
    }

    public CuisineTypeNotValidV2Exception(String message) {
        super("The request with " + message + " was failed, please try again");
    }

    @Override
    public Response toResponse(CuisineTypeNotValidV2Exception e) {

        String errorResponse = "{"
                + "  \"success\": " + false + ","
                + "  \"api_failed\": \"" + e.getMessage() + "\","
                + "  \"api_status\": \"400\""
                + "}";

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
