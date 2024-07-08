package com.example.bookwise.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookRecommendResponse {

    List<String> wishcategories;
    List<BookByMlDto>userRecommend;
    List<BookByMlDto>preferOne;
    List<BookByMlDto>preferTwo;



}


