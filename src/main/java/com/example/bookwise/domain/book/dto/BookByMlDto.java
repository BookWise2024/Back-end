package com.example.bookwise.domain.book.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookByMlDto {

    private String isbn13;
    private String coverURL;
}
