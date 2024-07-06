package com.example.bookwise.domain.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HasBookDto {
    private String hasBook;         // 도서 보유 여부
    private String loanAvailable;     // 도서 대출 여부
}
