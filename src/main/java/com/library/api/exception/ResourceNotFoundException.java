package com.library.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -546554116751405989L;

    /**
     * Constructor
     * @param message error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor
     * @param message error message
     * @param cause cause of the error
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
