package com.example.bookwise.domain.user.controller;

import com.example.bookwise.domain.oauth.jwt.JwtTokenProvider;
import com.example.bookwise.domain.user.dto.UserResponse;
import com.example.bookwise.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "유저")
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;



    @Operation(summary = "내 정보 조회")
    @GetMapping("/profile")
    public UserResponse getMemberProfile(@RequestHeader("Authorization") String accessToken) throws IOException {

            Long userId = jwtTokenProvider.extractId(accessToken);

            return userService.getUser(userId);
    }



}


