package com.example.bookwise.domain.oauth.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthLogoutService {

    private static final String LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";

    public String logout(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();

        RestTemplate restTemplate= new RestTemplate();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(LOGOUT_URL, request, String.class);

        return response.getBody();

    }

}
