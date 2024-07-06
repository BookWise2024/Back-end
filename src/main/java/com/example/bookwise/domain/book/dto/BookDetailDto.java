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
}
