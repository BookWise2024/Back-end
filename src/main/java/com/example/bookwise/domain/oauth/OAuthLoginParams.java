package com.example.bookwise.domain.oauth;

import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    MultiValueMap<String, String> makeBody();

}
