package org.example.back.services.exception;

public class JobLevelOutOfRangeException extends RuntimeException {
    public JobLevelOutOfRangeException(String message) {
        super(message);
    }
}
