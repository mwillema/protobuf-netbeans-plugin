package com.marcowillemart.common.util;

/**
 * FailureException is a runtime exception that should be thrown when a
 * contract or an invariant is broken.
 *
 * @author mwi
 */
public class FailureException extends RuntimeException {

    /**
     * @effects Initializes this to be a new failure exception with the
     *          provided message.
     */
    public FailureException(String message) {
        super(message);
    }

    /**
     * @effects Initializes this to be a new failure exception with the
     *          provided message and cause.
     */
    public FailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
