package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(Class<?> resourceClass, String details) {
        super(
                String.format(
                        "The request input for %s is invalid, details: %s",
                        resourceClass.getSimpleName(), details));
    }
}
