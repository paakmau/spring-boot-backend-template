package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.entity.Book;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repo.BookRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookRepo repo;

    public BookServiceImpl(BookRepo repo) {
        this.repo = repo;
    }

    @Override
    public BookDto create(BookDto dto) {
        return BookMapper.INSTANCE.toDto(repo.save(BookMapper.INSTANCE.toEntity(dto)));
    }

    @Override
    public void delete(long id) {
        if (repo.findById(id).isEmpty()) {
            logger.warn("Can't delete Book by Id {}", id);
            throw new NotFoundException(Book.class, new String[] {"Id"});
        }
        repo.deleteById(id);
    }

    @Override
    public BookDto update(long id, BookDto dto) {
        if (repo.findById(id).isEmpty()) {
            logger.warn("Can't update Book by Id {}", id);
            throw new NotFoundException(Book.class, new String[] {"Id"});
        }
        dto.setId(id);
        return BookMapper.INSTANCE.toDto(repo.save(BookMapper.INSTANCE.toEntity(dto)));
    }

    @Override
    public BookDto get(long id) {
        Optional<Book> book = repo.findById(id);
        if (book.isEmpty()) {
            logger.warn("Can't get Book by Id {}", id);
            throw new NotFoundException(Book.class, new String[] {"Id"});
        }
        return BookMapper.INSTANCE.toDto(book.get());
    }

    @Override
    public List<BookDto> getByTitle(String title) {
        return repo.findByTitle(title).stream().map(BookMapper.INSTANCE::toDto).toList();
    }
}
