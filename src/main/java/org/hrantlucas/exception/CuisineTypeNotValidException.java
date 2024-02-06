package org.hrantlucas.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CuisineTypeNotValidException extends Exception implements ExceptionMapper<CuisineTypeNotValidException> {

    public CuisineTypeNotValidException() {
        super("Provided cuisine type is invalid or unknown, please try again");
    }

    public CuisineTypeNotValidException(String message) {
        super(message + " is an invalid or unknown cuisine type, please try again");
    }

    @Override
    public Response toResponse(CuisineTypeNotValidException e) {
        return Response.status(400).entity(e.getMessage())
                .type(MediaType.APPLICATION_XML).build();
    }
}
