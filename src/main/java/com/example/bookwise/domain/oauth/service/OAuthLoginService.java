package com.example.bookwise.domain.oauth.service;


import com.example.bookwise.domain.oauth.AuthTokens;
import com.example.bookwise.domain.oauth.AuthTokensGenerator;
import com.example.bookwise.domain.oauth.dto.OAuthInfoResponse;
import com.example.bookwise.domain.oauth.OAuthLoginParams;
//import com.example.bookwise.domain.oauth.jwt.JwtTokenProvider;
//import com.example.bookwise.domain.redis.AccessToken;
//import com.example.bookwise.domain.redis.RedisUtil;
import com.example.bookwise.domain.redis.AccessToken;
import com.example.bookwise.domain.redis.RedisUtil;
import com.example.bookwise.domain.user.entity.User;
import com.example.bookwise.domain.user.repository.UserRepository;
import com.example.bookwise.domain.wishcategory.service.WishcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class OAuthLoginService {
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final UserRepository userRepository;
    private final WishcategoryService wishcategoryService;
    private final RedisUtil redisUtil;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분



    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long userId = findOrCreateUser(oAuthInfoResponse);
        AuthTokens authTokens = authTokensGenerator.generate(userId);
        AccessToken accessToken = new AccessToken(authTokens.getAccessToken(),String.valueOf(userId));

//        redisUtil.set(authTokens.getAccessToken(),accessToken, (int)ACCESS_TOKEN_EXPIRE_TIME);
        return authTokens;
    }

    private Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {

        return userRepository.findByEmail(oAuthInfoResponse.getEmail()).map(User::getUserId).orElseGet(() -> newUser(oAuthInfoResponse));

    }

    private Long newUser(OAuthInfoResponse oAuthInfoResponse) {


        User user = User.builder().email(oAuthInfoResponse.getEmail()).nickname(oAuthInfoResponse.getNickname()).build();
        Long userId = userRepository.save(user).getUserId();

        wishcategoryService.createWishcategory(userId);

        return userId;
    }

}
