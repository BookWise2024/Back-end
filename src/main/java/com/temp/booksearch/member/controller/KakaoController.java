package com.temp.booksearch.member.controller;

import com.temp.booksearch.member.VO.MemberVO;
import com.temp.booksearch.member.service.KakaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
@RequestMapping("/api/auth/kakao")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // CORS 설정 추가
public class KakaoController {

    @Autowired
    private KakaoService kakaoService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public ResponseEntity<Void> home() throws URISyntaxException {
        URI redirectUri = new URI("http://localhost:5173");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @PostMapping("/token")
    public Map<String, Object> getToken(@RequestParam String code) {
        return kakaoService.getToken(code);
    }

    @PostMapping("/callback")
    public ModelAndView callback(@RequestParam String code, HttpSession session) {
        Map<String, Object> token = kakaoService.getToken(code);
        String accessToken = (String) token.get("access_token");
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        // Save user info in session
        MemberVO user = kakaoService.saveUser(userInfo);
        session.setAttribute("user", user);

        // Authenticate the user
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getUserName(), null, new ArrayList<>()));

        return new ModelAndView("redirect:http://localhost:5173/home");
    }
}
