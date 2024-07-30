package com.example.bookwise.domain.book.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookDisplayDto {


    private String title;
    private String bookId;
    private String coverUrl;

}
