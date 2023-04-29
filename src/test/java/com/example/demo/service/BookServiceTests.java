package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.dto.BookDto;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repo.BookRepo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
class BookServiceTests {
    private static List<Book> books;
    private static List<BookDto> bookDtos;

    @MockBean private BookRepo repo;

    @Autowired private BookService service;

    @BeforeAll
    static void initAll() {
        books =
                Arrays.asList(
                        new Book(1L, "Book 1", "Author 1"),
                        new Book(2L, "Book 2", "Author 2"),
                        new Book(3L, "Book 3", "Author 3"));

        bookDtos = books.stream().map(BookMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @BeforeEach
    void init() {}

    @Test
    void testGet() {
        for (Book book : books) {
            Mockito.when(repo.findById(book.getId())).thenReturn(Optional.of(book));
        }
        for (BookDto dto : bookDtos) {
            assertEquals(dto, service.get(dto.getId()));
        }
    }

    @Test
    void testGetByTitle() {
        for (Book book : books) {
            Mockito.when(repo.findByTitle(book.getTitle())).thenReturn(Arrays.asList(book));
        }
        for (BookDto dto : bookDtos) {
            List<BookDto> vos = service.getByTitle(dto.getTitle());
            assertEquals(1, vos.size());
            assertEquals(dto, vos.get(0));
        }
    }

    @AfterEach
    void tearDown() {}

    @AfterAll
    static void tearDownAll() {}
}
