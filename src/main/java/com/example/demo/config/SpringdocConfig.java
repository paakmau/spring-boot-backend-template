package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class SpringdocConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI();
    }

    @Bean
    public OpenApiCustomiser globalHeaderOpenApiCustomiser() {
        return openApi ->
                openApi.getPaths()
                        .values()
                        .forEach(
                                pathItem -> {
                                    addResponses(
                                            Optional.ofNullable(pathItem.getPost()),
                                            HttpStatus.BAD_REQUEST);
                                    addResponses(
                                            Optional.ofNullable(pathItem.getDelete()),
                                            HttpStatus.NOT_FOUND);
                                    addResponses(
                                            Optional.ofNullable(pathItem.getPut()),
                                            HttpStatus.NOT_FOUND);
                                    addResponses(
                                            Optional.ofNullable(pathItem.getGet()),
                                            HttpStatus.NOT_FOUND);
                                });
    }

    private void addResponses(Optional<Operation> operation, HttpStatus... statuses) {
        operation.stream()
                .forEach(
                        o ->
                                Arrays.stream(statuses)
                                        .forEach(
                                                s ->
                                                        o.getResponses()
                                                                .addApiResponse(
                                                                        String.valueOf(s.value()),
                                                                        new ApiResponse()
                                                                                .description(
                                                                                        s
                                                                                                .getReasonPhrase()))));
    }
}
