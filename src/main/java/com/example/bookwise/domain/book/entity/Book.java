package com.example.bookwise.domain.book.entity;

import com.example.bookwise.domain.book.dto.BookDetailDto;
import com.example.bookwise.domain.wishilist.entity.Wishlist;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class Book {

    @Id
    private String bookId;

   private String coverUrl;

    private String title;

    private String author;

    private String styleDesc;

    private String publishDate;

    private String publisher;

    private String category;

    private String subcategory;

    private String description;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private List<Wishlist> wishlist = new ArrayList<>();

    public Book(String bookId, String coverUrl, String title, String author, String styleDesc, String publishDate, String publisher, String category, String subcategory, String description ){
        this.bookId = bookId;
        this.coverUrl = coverUrl;
        this.title = title;
        this.author = author;
        this.styleDesc = styleDesc;
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.category = category;
        this.subcategory = subcategory;
        this.description = description;
    }

    public Book(BookDetailDto bookAladin) {
        this.bookId = bookAladin.getBookId();
        this.description = bookAladin.getDescription();
        this.subcategory = bookAladin.getSubcategory();
        this.category = bookAladin.getCategory();
        this.coverUrl = bookAladin.getCoverUrl();
        this.publisher = bookAladin.getPublisher();
        this.styleDesc =bookAladin.getStyleDesc();
        this.publishDate = bookAladin.getPublishDate();
        this.title = bookAladin.getTitle();
        this.author = bookAladin.getAuthor();
    }
}
