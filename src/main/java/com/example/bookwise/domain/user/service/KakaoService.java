//package com.example.bookwise.domain.user.service;
//
//import com.example.bookwise.domain.user.DAO.UserRepository;
//import com.example.bookwise.domain.user.VO.UserVO;
//import lombok.Getter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class KakaoService {
//
//    @Value("${kakao.client-id}")
//    @Getter
//    private String clientId;
//
//    @Value("${kakao.client-secret}")
//    @Getter
//    private String clientSecret;
//
//    @Value("${kakao.redirect-uri}")
//    @Getter
//    private String redirectUri;
//
//    @Autowired
//    private UserRepository userRepository;
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public Map<String, Object> getToken(String code) {
//        // token 요청하는 엔드 포인트 url
//        String tokenUrl = "https://kauth.kakao.com/oauth/token";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        // 앱 키
//        params.add("client_id", clientId);
//        // redirect url
//        params.add("redirect_uri", redirectUri);
//        // 인가 코드
//        params.add("code", code);
//        params.add("client_secret", clientSecret);
//
//        // HTTP 요청 엔티티 생성
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//        // POST 요청을 보내고 응답을 받아옴
//        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
//
//        return response.getBody();
//    }
//
//    public Map<String, Object> getUserInfo(String accessToken) {
//        // 사용자 정보 요청하는 엔드 포인트 url
//        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//
//        HttpEntity<Void> request = new HttpEntity<>(headers);
//
//        ResponseEntity<Map> response =
//                restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);
//
//        return response.getBody();
//    }
//
//    public UserVO saveUser(Map<String, Object> userInfo) {
//        String username = userInfo.get("id").toString();
//        Map<String, String> kakaoAccount = (Map<String, String>) userInfo.get("kakao_account");
//        String useremail = kakaoAccount.get("email");
//
//        // 사용자 정보가 있으면 그 정보를 database에서 가져오고 없으면 새로 저장
//        UserVO user = userRepository.findByUserName(username);
//        if (user == null) {
//            user = new UserVO();
//            user.setUserName(username);
//            user.setUserEmail(useremail);
//            userRepository.save(user);
//        }
//        return user;
//    }
//}
//
