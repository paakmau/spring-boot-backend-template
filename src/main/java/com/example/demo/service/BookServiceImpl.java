package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.entity.Book;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repo.BookRepo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private BookRepo repo;

    @Override
    public BookDto create(BookDto dto) {
        if (dto.getId() != null) {
            logger.warn("The Id of Book is not null");
            throw new InvalidInputException(Book.class, "Id is not null");
        }
        return BookDto.fromEntity(repo.save(dto.toBook()));
    }

    @Override
    public void delete(Long id) {
        if (repo.findById(id).isEmpty()) {
            logger.warn("Can't delete Book by Id {}", id);
            throw new NotFoundException(Book.class, new String[] {"Id"});
        }
        repo.deleteById(id);
    }

    @Override
    public BookDto update(Long id, BookDto dto) {
        if (repo.findById(id).isEmpty()) {
            logger.warn("Can't update Book by Id {}", id);
            throw new NotFoundException(Book.class, new String[] {"Id"});
        }
        dto.setId(id);
        return BookDto.fromEntity(repo.save(dto.toBook()));
    }

    @Override
    public BookDto get(Long id) {
        Optional<Book> book = repo.findById(id);
        if (book.isEmpty()) {
            logger.warn("Can't get Book by Id {}", id);
            throw new NotFoundException(Book.class, new String[] {"Id"});
        }
        return BookDto.fromEntity(book.get());
    }

    @Override
    public List<BookDto> getByTitle(String title) {
        List<BookDto> books =
                repo.findByTitle(title).stream()
                        .map(BookDto::fromEntity)
                        .collect(Collectors.toList());
        if (books.isEmpty()) {
            logger.warn("Can't get Book by Title {}", title);
            throw new NotFoundException(Book.class, new String[] {"Title"});
        }
        return books;
    }
}
