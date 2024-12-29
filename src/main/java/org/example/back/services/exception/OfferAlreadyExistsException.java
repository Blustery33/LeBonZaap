package org.example.back.services.exception;

public class OfferAlreadyExistsException extends RuntimeException {
    public OfferAlreadyExistsException(String message) {
        super(message);
    }
}

