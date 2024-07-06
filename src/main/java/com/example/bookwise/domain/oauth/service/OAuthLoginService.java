package com.example.bookwise.domain.oauth.service;


import com.example.bookwise.domain.oauth.AuthTokens;
import com.example.bookwise.domain.oauth.AuthTokensGenerator;
import com.example.bookwise.domain.oauth.dto.OAuthInfoResponse;
import com.example.bookwise.domain.oauth.OAuthLoginParams;
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


    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long userId = findOrCreateUser(oAuthInfoResponse);
        return authTokensGenerator.generate(userId);
    }

    private Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {

        return userRepository.findByEmail(oAuthInfoResponse.getEmail()).map(User::getUserId).orElseGet(() -> newUser(oAuthInfoResponse));

    }

    private Long newUser(OAuthInfoResponse oAuthInfoResponse) {


        User user = User.builder().email(oAuthInfoResponse.getEmail()).build();
        Long userId = userRepository.save(user).getUserId();

        wishcategoryService.createWishcategory(userId);

        return userId;
    }

}
