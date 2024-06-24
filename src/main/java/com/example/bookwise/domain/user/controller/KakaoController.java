package com.example.bookwise.domain.user.controller;

import com.example.bookwise.domain.user.VO.UserVO;
import com.example.bookwise.domain.user.service.KakaoService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // CORS 설정 추가
public class KakaoController {

    private static final Logger log = LoggerFactory.getLogger(KakaoController.class);

    @Autowired
    private KakaoService kakaoService;

    @GetMapping("/")
    public ResponseEntity<Void> home() throws URISyntaxException {
        URI redirectUri = new URI("http://localhost:5173");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    // 백에서 바로 kakao login 요청을 보내는 메소드
//    @GetMapping("/login")
//    public ResponseEntity<Void> login() throws URISyntaxException {
//        String redirectUrl = "https://kauth.kakao.com/oauth/authorize?client_id="
//                + kakaoService.getClientId() + "&redirect_uri="
//                + kakaoService.getRedirectUri() + "&response_type=code";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(new URI(redirectUrl));
//        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
//    }

    @PostMapping("/api/auth/kakao/callback")
    public ResponseEntity<Void> callback(@RequestBody Map<String, String> requestBody, HttpSession session) {
        try {
            String code = requestBody.get("code");
            log.info("Access Succeed");

            Map<String, Object> token = kakaoService.getToken(code);
            String accessToken = (String) token.get("access_token");
            Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

            // 사용자 정보를 세션에 저장
            UserVO user = kakaoService.saveUser(userInfo);
            session.setAttribute("user", user);

            // 사용자 인증
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user.getUserName(), null, new ArrayList<>());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 로그 추가
            log.info("User authenticated: " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(
                    URI.create("http://localhost:5173/")); // 클라이언트가 이동할 경로 설정

            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            log.error("Error during callback processing", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
