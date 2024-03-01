package org.hrantlucas.exception.v2;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericV2ExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        String errorResponse = "{"
                + "  \"success\": " + false + ","
                + "  \"api_failed\": \"" + exception.getMessage() + "\","
                + "  \"api_status\": \"500\""
                + "}";

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
