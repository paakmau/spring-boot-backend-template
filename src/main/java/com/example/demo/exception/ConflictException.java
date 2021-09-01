package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    public ConflictException(Class<?> resourceClass, String details) {
        super(
                String.format(
                        "The request for %s conflicts, details: %s",
                        resourceClass.getSimpleName(), details));
    }
}
