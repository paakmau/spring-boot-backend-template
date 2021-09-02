package com.example.demo.config;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.springframework.boot").negate())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponses(
                        HttpMethod.POST,
                        responses(Collections.singletonList(HttpStatus.BAD_REQUEST)))
                .globalResponses(
                        HttpMethod.DELETE,
                        responses(Collections.singletonList(HttpStatus.NOT_FOUND)))
                .globalResponses(
                        HttpMethod.PUT, responses(Collections.singletonList(HttpStatus.NOT_FOUND)))
                .globalResponses(
                        HttpMethod.GET, responses(Collections.singletonList(HttpStatus.NOT_FOUND)));
    }

    private List<Response> responses(List<HttpStatus> statuses) {
        return statuses.stream()
                .map(
                        status ->
                                new ResponseBuilder()
                                        .code(String.valueOf(status.value()))
                                        .description(status.getReasonPhrase())
                                        .build())
                .collect(Collectors.toList());
    }
}
