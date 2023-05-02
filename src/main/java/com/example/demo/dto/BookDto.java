package com.example.demo.dto;

import com.example.demo.validation.OnCreate;
import com.example.demo.validation.OnUpdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record BookDto(
        @Null(groups = OnCreate.class) @NotNull(groups = OnUpdate.class) Long id,
        @NotBlank String title,
        @NotBlank String author) {
    public BookDto withId(Long id) {
        return new BookDto(id, title, author);
    }
}
