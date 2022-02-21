package com.example.demo.dto;

import com.example.demo.entity.Book;
import java.util.Objects;

public class BookDto {
    private Long id;

    private String title;

    private String author;

    public BookDto() {}

    public BookDto(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public static BookDto fromEntity(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Book toBook() {
        return new Book(this.id, this.title, this.author);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BookDto)) return false;
        BookDto other = (BookDto) o;
        if (this.id == null ? other.id != null : !this.id.equals(other.id)) return false;
        if (this.title == null ? other.title != null : !this.title.equals(other.title))
            return false;
        return !(this.author == null ? other.author != null : !this.author.equals(other.author));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.author);
    }
}
