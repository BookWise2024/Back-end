package com.example.bookwise.domain.library.dto;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class LibraryInitDBDto {
    @Value("${librarybigdata.api.key}")
    private String libraryBigdataKey; //도서관정보나루 인증키

    @Value("${seoullibrary.api.key}")
    private String seoulLibraryKey; // 서울공공도서관 인증키
    private String pageNo = "1"; //페이지 번호
    private String pageSize = "319"; //페이지 사이즈
    private String region = "11"; // 서울지역번호
    private String format = "json"; // 포맷 형식

}


