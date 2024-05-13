package ru.example.client.exception;

public class CustomDataAccessException extends RuntimeException {
    public CustomDataAccessException(String message) {
        super(message);
    }
}
