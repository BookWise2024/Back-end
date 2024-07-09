package com.example.bookwise.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookRecommendDto {
    List<String> user_preferences;
//    List<BookClickDto> bookClickDtos;

    Map<String, Long> clicksMap;

    public BookRecommendDto(ArrayList<String> bookList,Map<String, Long> clicksMap) {
        this.user_preferences = bookList;
        this.clicksMap = clicksMap;
    }
}
