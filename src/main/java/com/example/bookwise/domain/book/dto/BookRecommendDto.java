package com.example.bookwise.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookRecommendDto {
    List<String> user_preferences;
    List<BookWishCategoryDto> bookWishCategoryDtos;
    List<BookClickDto> bookClickDtos;

    public BookRecommendDto(ArrayList<String> bookList, List<BookWishCategoryDto> bookWishCategoryDtos, List<BookClickDto> bookClickDtos) {
        this.user_preferences = bookList;
        this.bookWishCategoryDtos = bookWishCategoryDtos;
        this.bookClickDtos = bookClickDtos;
    }
}
