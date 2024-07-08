package com.example.bookwise.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookRecommendByCategoryDto {

    List<BookByMlDto> first;
    List<BookByMlDto> second;

//    public BookRecommendByCategoryDto(List<BookByMlDto> first,List<BookByMlDto> second) {
//        this.first = first;
//        this.second =second;
//    }
}
