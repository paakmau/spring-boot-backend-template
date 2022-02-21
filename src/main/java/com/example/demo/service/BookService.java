package com.example.demo.service;

import com.example.demo.dto.BookDto;
import java.util.List;

public interface BookService {
    BookDto create(BookDto dto);

    void delete(Long id);

    BookDto update(Long id, BookDto dto);

    BookDto get(Long id);

    List<BookDto> getByTitle(String title);
}
