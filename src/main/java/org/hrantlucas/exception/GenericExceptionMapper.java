package org.hrantlucas.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        String errorResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<error>" +
                "<errorCode>" + "500" + "</errorCode>" +
                "<errorMessage>" + exception.getMessage() + ", an error has occurred, please try again" + "</errorMessage>" +
                "</error>";

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_XML)
                .build();
    }
}
