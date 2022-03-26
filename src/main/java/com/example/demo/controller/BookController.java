package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.service.BookService;
import com.example.demo.validation.OnCreate;
import com.example.demo.validation.OnUpdate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/books")
@Validated
public class BookController {
    @Autowired private BookService service;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public BookDto post(@RequestBody @Validated(OnCreate.class) @Valid BookDto dto) {
        return service.create(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(1) long id) {
        service.delete(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public BookDto put(
            @PathVariable @Min(1) long id,
            @RequestBody @Validated(OnUpdate.class) @Valid BookDto dto) {
        return service.update(id, dto);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public BookDto get(@PathVariable @Min(1) long id) {
        return service.get(id);
    }

    @GetMapping
    public List<BookDto> getByTitle(@RequestParam @NotBlank String title) {
        return service.getByTitle(title);
    }
}
