package com.example.demo.exception;

import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(Class<?> resourceClass, String[] conditions) {
        super(
                String.format(
                        "The request for %s failed, find by %s",
                        resourceClass.getSimpleName(), Arrays.toString(conditions)));
    }
}
