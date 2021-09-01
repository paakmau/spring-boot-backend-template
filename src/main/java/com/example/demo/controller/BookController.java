package com.example.demo.controller;

import com.example.demo.service.BookService;
import com.example.demo.vo.BookVo;
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
@RequestMapping(value = "/book")
public class BookController {
    @Autowired private BookService service;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Long post(@RequestBody BookVo vo) {
        return service.create(vo);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void put(@PathVariable Long id, @RequestBody BookVo vo) {
        service.update(id, vo);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public BookVo get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<BookVo> getByTitle(@RequestParam String title) {
        return service.getByTitle(title);
    }
}
