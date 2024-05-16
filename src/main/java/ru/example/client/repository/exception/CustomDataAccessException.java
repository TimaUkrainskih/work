package ru.example.client.repository.exception;

public class CustomDataAccessException extends RuntimeException {
    public CustomDataAccessException(String message) {
        super(message);
    }
}
