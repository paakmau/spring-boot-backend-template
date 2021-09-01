package com.example.demo.repo;

import com.example.demo.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);
}
