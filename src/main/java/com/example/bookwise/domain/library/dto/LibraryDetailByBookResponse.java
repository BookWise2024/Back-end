package com.example.bookwise.domain.library.dto;

import com.example.bookwise.domain.book.dto.BookDetailDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LibraryDetailByBookResponse {
    LibraryDetailResponse library;
    // 책 데이터
    HasBookDetailDto book;

    private List<BookDetailDto> similarBooks;
}




