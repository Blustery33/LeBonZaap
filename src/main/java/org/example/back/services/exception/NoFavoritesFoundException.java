package org.example.back.services.exception;

public class NoFavoritesFoundException extends RuntimeException {
    public NoFavoritesFoundException(String message) {
        super(message);
    }
}
