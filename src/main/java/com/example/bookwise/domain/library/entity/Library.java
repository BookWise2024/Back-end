package com.example.bookwise.domain.library.entity;

import com.example.bookwise.domain.library.dto.LibraryInitDBDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

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
    @Column(length = 65535)
    private String opTime;      // 운영시간
    private String closeTime;   // 공휴일
    private Double latitude;    // 위도
    private Double longitude;   // 경도


    // Haversine 공식 사용(km기준)
    public static Double getDistance(Double x1, Double y1, Double x2, Double y2) {
        double distance;
        double radius = 6371; // 지구 반지름(km)
        double toRadian = Math.PI / 180;

        double deltaLatitude = Math.abs(x1 - x2) * toRadian;
        double deltaLongitude = Math.abs(y1 - y2) * toRadian;

        double sinDeltaLat = Math.sin(deltaLatitude / 2);
        double sinDeltaLng = Math.sin(deltaLongitude / 2);
        double squareRoot = Math.sqrt(
                sinDeltaLat * sinDeltaLat +
                        Math.cos(x1 * toRadian) * Math.cos(x2 * toRadian) * sinDeltaLng * sinDeltaLng);

        distance = (2 * radius * Math.asin(squareRoot));

        return distance;
    }


}


