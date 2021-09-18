package com.example.demo.service.impl;

import com.example.demo.entity.Book;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repo.BookRepo;
import com.example.demo.service.BookService;
import com.example.demo.vo.BookVo;
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
    public BookVo create(BookVo vo) {
        if (vo.getId() != null) {
            logger.warn("The Id of Book is not null");
            throw new InvalidInputException(Book.class, "Id is not null");
        }
        return BookVo.fromEntity(repo.save(vo.toBook()));
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
    public BookVo update(Long id, BookVo vo) {
        if (repo.findById(id).isEmpty()) {
            logger.warn("Can't update Book by Id {}", id);
            throw new NotFoundException(Book.class, new String[] {"Id"});
        }
        vo.setId(id);
        return BookVo.fromEntity(repo.save(vo.toBook()));
    }

    @Override
    public BookVo get(Long id) {
        Optional<Book> book = repo.findById(id);
        if (book.isEmpty()) {
            logger.warn("Can't get Book by Id {}", id);
            throw new NotFoundException(Book.class, new String[] {"Id"});
        }
        return BookVo.fromEntity(book.get());
    }

    @Override
    public List<BookVo> getByTitle(String title) {
        List<BookVo> books =
                repo.findByTitle(title).stream()
                        .map(BookVo::fromEntity)
                        .collect(Collectors.toList());
        if (books.isEmpty()) {
            logger.warn("Can't get Book by Title {}", title);
            throw new NotFoundException(Book.class, new String[] {"Title"});
        }
        return books;
    }
}
