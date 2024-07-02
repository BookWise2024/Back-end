package com.example.bookwise.domain.wishilist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WishlistResponse {

    private int count;

    private List<WishlistBookDto> bookList;

}