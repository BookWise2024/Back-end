package com.example.bookwise.domain.bookclick.repository;

import com.example.bookwise.domain.bookclick.entity.BookClick;
import com.example.bookwise.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookClickRepository extends JpaRepository<BookClick, Long> {
    BookClick findByUserAndBookId(User user, String bookId);

    List<BookClick> findByUserUserId(Long userId);
}
