package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class BookController {
    @Autowired private BookService service;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public BookDto post(@RequestBody BookDto dto) {
        return service.create(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public BookDto put(@PathVariable Long id, @RequestBody BookDto dto) {
        return service.update(id, dto);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public BookDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<BookDto> getByTitle(@RequestParam String title) {
        return service.getByTitle(title);
    }
}
