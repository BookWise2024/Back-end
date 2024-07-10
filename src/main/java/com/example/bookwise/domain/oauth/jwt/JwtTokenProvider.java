package com.example.bookwise.domain.oauth.jwt;


import com.example.bookwise.global.error.CustomForbiddenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j

public class JwtTokenProvider {

    private final Key key;

//    private final RedisUtil redisUtil;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
//        this.redisUtil = redisUtil;
        //    this.redisUtil = redisUtil;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 생성
    public String generate(String subject, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // accessToken에서 userId를 추출함
    public String extractSubject(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long extractId(String accessToken) {
        if (!validAccessToken(accessToken)) {
            throw new CustomForbiddenException("유효하지 않은 토큰입니다.");
        }


        String subject = extractSubject(accessToken);
        return Long.parseLong(subject);
    }

    public boolean validAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken);
//            if(redisUtil.hasKeyBlackList(accessToken)) {        // 로그아웃 끝난 토큰
//                log.info("로그아웃된 토큰입니다.");
//                return false;
//            }
            return true;  //유효하다면 true 반환
        } catch (MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 토큰입니다.");
            return false;
        } catch(ExpiredJwtException e){
            log.info("만료된 토큰입니다.");
            return false;
        } catch (IllegalArgumentException e) {
            log.info("토큰이 잘못되었습니다.");
            return false;
    }
    }

    public boolean validRefreshToken(String refreshToken) throws Exception {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();
            return true;  //유효하다면 true 반환
        } catch (MalformedJwtException e) {
            throw new Exception();
        } catch (ExpiredJwtException e) {
            throw new Exception();
        }
    }

    public Long getExpiration(String accessToken) {
        Claims claims= Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        return claims.getExpiration().getTime();
    }




    private String getUsername(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }
}