package com.example.bookwise.domain.oauth.controller;

import com.example.bookwise.domain.oauth.service.OAuthLoginService;
import com.example.bookwise.domain.oauth.AuthTokens;
import com.example.bookwise.domain.oauth.service.OAuthLogoutService;
import com.example.bookwise.domain.kakao.KakaoLoginParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "카카오 로그인")
@RequestMapping("/api/auth")
public class AuthController {

    private final OAuthLoginService oAuthLoginService;
    private final OAuthLogoutService oAuthLogoutService;

    @Operation(summary = "카카오 로그인 및 회원가입")
    @PostMapping("/login")
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
        log.info("{}",params);
        AuthTokens authTokens = oAuthLoginService.login(params);



        return ResponseEntity.ok(authTokens);
    }


    // 미완성
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken) throws Exception {

        oAuthLogoutService.logout(accessToken);
        return ResponseEntity.ok("로그아웃 완료");
    }



}
