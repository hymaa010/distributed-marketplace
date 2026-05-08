package org.team13.marketplace.exception;

/**
 * exception for all business logic errors.
 * Caught by ClientHandler (socket) and GlobalExceptionHandler (REST).
 */
public class MarketplaceException extends RuntimeException {

    public MarketplaceException(String message) {
        super(message);
    }

    public MarketplaceException(String message, Throwable cause) {
        super(message, cause);
    }
}
