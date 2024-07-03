package com.example.bookwise.domain.oauth;

import com.example.bookwise.domain.oauth.dto.OAuthInfoResponse;

public interface OAuthApiClient {
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);
}