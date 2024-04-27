package org.hrantlucas.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    private UriInfo uriInfo;
    @Override
    public Response toResponse(Throwable exception) {

        String requestPath = uriInfo.getAbsolutePath().getPath();
        String errorResponse;
        String mediaType;

        if(requestPath.contains("/v2/") || requestPath.contains("menu")) {
            mediaType = MediaType.APPLICATION_JSON;
            errorResponse = "{"
                    + "  \"success\": " + false + ","
                    + "  \"api_failed\": \"" + exception.getMessage() + "\","
                    + "  \"api_status\": \"500\""
                    + "}";
        }else {
            mediaType = MediaType.APPLICATION_XML;
            errorResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<error>" +
                    "<errorCode>" + "500" + "</errorCode>" +
                    "<errorMessage>" + exception.getMessage() + ", an error has occurred, please try again" + "</errorMessage>" +
                    "</error>";
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type(mediaType)
                .build();
    }
}
