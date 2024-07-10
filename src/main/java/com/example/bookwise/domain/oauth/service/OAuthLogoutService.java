package com.example.bookwise.domain.oauth.service;

import com.example.bookwise.domain.oauth.jwt.JwtTokenProvider;
import com.example.bookwise.global.error.CustomForbiddenException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OAuthLogoutService {

    private final JwtTokenProvider jwtTokenProvider;

//    private final RedisUtil redisUtil;
//    private final AccessTokenRepository accessTokenRepository;

    @Transactional
    public void logout(String accessToken) {

        //  Access Token 검증
        if (!jwtTokenProvider.validAccessToken(accessToken)) {
            throw new CustomForbiddenException("허용되지 않는 작업입니다");
        }

//        redisUtil.setBlackList(accessToken, accessToken, jwtTokenProvider.getExpiration(accessToken));

    }


}
