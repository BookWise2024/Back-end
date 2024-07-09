package com.example.bookwise.domain.book.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailDto {

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
    private String itemId;
    private String like;
//
//    public BookDetailDto(String bookId, String coverUrl, String title, String author, String styleDesc, String publishDate, String publisher, String category, String subcategory, String description,String like) {
//        this.bookId = bookId;
//        this.coverUrl = coverUrl;
//        this.title = title;
//        this.author = author;
//        this.styleDesc = styleDesc;
//        this.publishDate = publishDate;
//        this.publisher = publisher;
//        this.category = category;
//        this.subcategory = subcategory;
//        this.description = description;
//        this.like = like;
//    }

    public BookDetailDto(String bookId, String coverUrl, String title, String author, String styleDesc, String publishDate, String publisher, String category, String subcategory, String description,String itemId) {
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
        this.itemId = itemId;
    }
}
