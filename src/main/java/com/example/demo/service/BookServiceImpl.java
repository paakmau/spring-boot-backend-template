package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.entity.Book;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repo.BookRepo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private ModelMapper modelMapper;

    @Autowired private BookRepo repo;

    @Override
    public BookDto create(BookDto dto) {
        return modelMapper.map(repo.save(modelMapper.map(dto, Book.class)), BookDto.class);
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
        return modelMapper.map(repo.save(modelMapper.map(dto, Book.class)), BookDto.class);
    }

    @Override
    public BookDto get(long id) {
        Optional<Book> book = repo.findById(id);
        if (book.isEmpty()) {
            logger.warn("Can't get Book by Id {}", id);
            throw new NotFoundException(Book.class, new String[] {"Id"});
        }
        return modelMapper.map(book.get(), BookDto.class);
    }

    @Override
    public List<BookDto> getByTitle(String title) {
        return repo.findByTitle(title).stream()
                .map(b -> modelMapper.map(b, BookDto.class))
                .collect(Collectors.toList());
    }
}
