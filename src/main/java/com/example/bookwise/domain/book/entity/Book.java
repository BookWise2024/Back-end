package com.example.bookwise.domain.book.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

}
