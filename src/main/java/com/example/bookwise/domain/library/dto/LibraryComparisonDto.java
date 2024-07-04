package com.example.bookwise.domain.library.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LibraryComparisonDto {

    @Id
    private Long libraryId;     // 도서관 코드
    private String name;        // 이름
    private String address;     //  주소
    private String url;         // 도서관 홈페이지 url
    private String tel;
}
