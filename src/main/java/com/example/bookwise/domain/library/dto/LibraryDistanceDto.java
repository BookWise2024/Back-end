package com.example.bookwise.domain.library.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LibraryDistanceDto {

    private Long libraryId;
    private String name;        // 이름
    private String address;     //  주소
    private double distance;       // 거리
    private double latitude;    // 위도
    private double longitude;   // 경도


}
