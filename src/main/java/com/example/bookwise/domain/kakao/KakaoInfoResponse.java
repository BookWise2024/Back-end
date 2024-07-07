package com.example.bookwise.domain.kakao;

import com.example.bookwise.domain.oauth.dto.OAuthInfoResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

    @JsonProperty("id")
    private Long userId;

    @JsonProperty("kakao_account")
    private KakaoEmail kakaoEmail;

    @JsonProperty("properties")
    private KakaoNickname kakaoNickname;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoEmail {
        private String email;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoNickname {
        private String nickname;
    }


    @Override
    public Long getUserId(){return userId;}


    @Override
    public String getEmail() {
        return kakaoEmail.getEmail();
    }


    @Override
    public String getNickname() {
        return kakaoNickname.getNickname();
    }

}