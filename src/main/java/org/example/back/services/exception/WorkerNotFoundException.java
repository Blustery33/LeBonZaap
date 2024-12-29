package org.example.back.services.exception;

public class WorkerNotFoundException extends RuntimeException {

    // Constructeur avec un message personnalisé
    public WorkerNotFoundException(String message) {
        super(message);
    }
}
