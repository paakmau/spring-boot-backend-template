package com.example.demo.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.entity.Book;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DataJpaTest
class BookRepoTests {
    private final List<Book> books =
            Arrays.asList(
                    new Book(null, "Book 1", "Author 1"),
                    new Book(null, "Book 2", "Author 2"),
                    new Book(null, "Book 3", "Author 3"));

    @Autowired private BookRepo repo;

    private Map<Long, Book> bookMap;

    @BeforeAll
    static void initAll() {}

    @BeforeEach
    void init() {
        bookMap = repo.saveAll(books).stream().collect(Collectors.toMap(Book::getId, book -> book));
    }

    @Test
    void testGet() {
        assertEquals(bookMap.size(), repo.count());

        for (Book book : bookMap.values()) {
            assertEquals(book, repo.findById(book.getId()).get());
        }
    }

    @Test
    void testGetByTitle() {
        for (Book book : bookMap.values()) {
            List<Book> actualBooks = repo.findByTitle(book.getTitle());
            assertEquals(1, actualBooks.size());
            assertEquals(book, actualBooks.get(0));
        }
    }

    @AfterEach
    void tearDown() {
        repo.deleteAll();
    }

    @AfterAll
    static void tearDownAll() {}
}
