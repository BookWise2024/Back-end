package com.example.bookwise.domain.oauth.jwt;

import com.example.bookwise.global.error.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
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
        String subject = extractSubject(accessToken);
        return Long.parseLong(subject);
    }

//    public boolean vaildAccessToken(String accessToken) {
//        try {
//            Claims claims =  Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(accessToken)
//                    .getBody();
//            return true;  //유효하다면 true 반환
//        } catch (MalformedJwtException e) {
//            return false;
//        } catch (ExpiredJwtException e) {
//            return false;
//        }
//    }
//
//    public boolean vaildRefreshToken(String refreshToken) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(refreshToken)
//                    .getBody();
//            return true;  //유효하다면 true 반환
//        } catch (MalformedJwtException e) {
//            throw new RestApiException(ErrorCode.UNAUTHORIZED_REQUEST);
//        } catch (ExpiredJwtException e) {
//            throw new RestApiException(ErrorCode.VALID_TOKEN_EXPIRED);
//        }
//    }
}
