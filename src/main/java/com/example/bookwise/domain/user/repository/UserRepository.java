package com.example.bookwise.domain.user.repository;

import com.example.bookwise.domain.library.entity.Library;
import com.example.bookwise.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(Long userId);
    Optional<User> findByEmail(String email);

}