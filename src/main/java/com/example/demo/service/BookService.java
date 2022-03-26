package com.example.demo.service;

import com.example.demo.dto.BookDto;
import java.util.List;

public interface BookService {
    BookDto create(BookDto dto);

    void delete(long id);

    BookDto update(long id, BookDto dto);

    BookDto get(long id);

    List<BookDto> getByTitle(String title);
}
