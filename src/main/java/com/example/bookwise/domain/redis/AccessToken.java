//package com.example.bookwise.domain.redis;
//
//
//import lombok.Getter;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.redis.core.RedisHash;
//
//@Getter
//@RedisHash(value = "accessToken", timeToLive = 14440)       // 4시간
//public class AccessToken {
//    @Id
//    private final String accessToken;
//    private final String userId;
//
//    public AccessToken(String accessToken, String userId) {
//        this.accessToken = accessToken;
//        this.userId = userId;
//    }
//
//}
