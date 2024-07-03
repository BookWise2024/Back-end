package com.example.bookwise.domain.user.service;


import com.example.bookwise.domain.user.dto.UserResponse;
import com.example.bookwise.domain.user.entity.User;
import com.example.bookwise.domain.oauth.jwt.JwtTokenProvider;
import com.example.bookwise.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public void deleteUser(Long id) throws IOException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        }
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) throws IOException {

        Optional<User> user = userRepository.findById(userId);

        return new UserResponse(user.get().getUserId(), user.get().getEmail());


    }
}