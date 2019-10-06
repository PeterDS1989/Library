package com.library.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 4220915778686060196L;

    /**
     * Constructor
     * @param message error message
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructor
     * @param message error message
     * @param cause cause of the error
     */
    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
