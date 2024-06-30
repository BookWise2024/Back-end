package com.example.bookwise.domain.library.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LibraryDetailByBookResponse {
    LibraryDetailResponse library;
    // 책 데이터
    // 리뷰 데이터 추가 긍정 : 3개, 부정 :3개
   // private List<SimilarBook> similarBooks;
}




