package com.temp.booksearch.member.controller;

import com.temp.booksearch.member.service.KakaoService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/kakao")
@CrossOrigin(origins = "http://localhost:5173") // React 앱 주소
public class KakaoController {

    @Autowired
    private KakaoService kakaoService;

    @PostMapping("/callback")
    public Map<String, Object> callback(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        Map<String, Object> token = kakaoService.getToken(code);
        String accessToken = (String) token.get("access_token");
        return kakaoService.getUserInfo(accessToken);
    }
}
