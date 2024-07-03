package com.example.bookwise.domain.oauth.service;

import com.example.bookwise.domain.oauth.OAuthApiClient;
import com.example.bookwise.domain.oauth.OAuthLoginParams;
import com.example.bookwise.domain.oauth.dto.OAuthInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class RequestOAuthInfoService {
    private final OAuthApiClient client;

    public RequestOAuthInfoService(OAuthApiClient client) {
        this.client = client;
    }

    public OAuthInfoResponse request(OAuthLoginParams params) {
        String accessToken = client.requestAccessToken(params);
        return client.requestOauthInfo(accessToken);
    }
}
