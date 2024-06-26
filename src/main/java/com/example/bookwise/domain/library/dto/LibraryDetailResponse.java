package com.example.bookwise.domain.library.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LibraryDetailResponse {

    private String name;
    private String address;
    private String url;
    private String opTime;
    private String closeTime;
}
