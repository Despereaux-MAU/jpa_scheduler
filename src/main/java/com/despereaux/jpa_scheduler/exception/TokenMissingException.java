package com.despereaux.jpa_scheduler.exception;

public class TokenMissingException extends RuntimeException {
    public TokenMissingException(String message) {
        super(message);
    }
}
