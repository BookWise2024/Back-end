package com.example.bookwise.domain.library.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class HasBookDetailDto {

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
    private String hasBook;         // 도서 보유 여부
    private String loanAvailable;     // 도서 대출 여부
}
