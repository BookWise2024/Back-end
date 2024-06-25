package com.example.bookwise.domain.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Library {

    @Id
    private Long libraryId;     // 도서관 코드
    private String name;        // 이름
    private String address;     //  주소
    private String url;         // 도서관 홈페이지 url
    @Column(length=65535)
    private String opTime;      // 운영시간
    private String closeTime;   // 공휴일
    private Double latitude;    // 위도
    private Double longitude;   // 경도


}


