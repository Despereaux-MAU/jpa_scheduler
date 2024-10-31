package com.despereaux.jpa_scheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public String handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TokenMissingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public String handleTokenMissingException(TokenMissingException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public String handleTokenExpiredException(TokenExpiredException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public String handleAccessDeniedException(AccessDeniedException ex) {
        return ex.getMessage();
    }
}
