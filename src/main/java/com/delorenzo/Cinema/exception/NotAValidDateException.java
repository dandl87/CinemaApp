package com.delorenzo.Cinema.exception;

public class NotAValidDateException extends RuntimeException {
    public NotAValidDateException(String message) {
        super(message);
    }
}
