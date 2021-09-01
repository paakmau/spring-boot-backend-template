package com.example.demo.service;

import com.example.demo.vo.BookVo;
import java.util.List;

public interface BookService {
    Long create(BookVo vo);

    void delete(Long id);

    void update(Long id, BookVo vo);

    BookVo get(Long id);

    List<BookVo> getByTitle(String title);
}
