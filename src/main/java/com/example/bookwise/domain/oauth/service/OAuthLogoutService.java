package com.example.bookwise.domain.oauth.service;

import com.example.bookwise.domain.oauth.jwt.JwtTokenProvider;
//import com.example.bookwise.domain.redis.RedisUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthLogoutService {

    private final JwtTokenProvider jwtTokenProvider;

 //   private final RedisUtil redisUtil;
//    private final AccessTokenRepository accessTokenRepository;

    @Transactional
    public void logout(String accessToken)  {

        //  Access Token 검증
        if (!jwtTokenProvider.validAccessToken(accessToken)) {
            throw new JwtException("JwtError");
        }


  //      redisUtil.setBlackList(accessToken,accessToken,jwtTokenProvider.getExpiration(accessToken));

    }
    

}
