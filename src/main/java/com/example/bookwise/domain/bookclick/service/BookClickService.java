package com.example.bookwise.domain.bookclick.service;

import com.example.bookwise.domain.bookclick.entity.BookClick;
import com.example.bookwise.domain.bookclick.repository.BookClickRepository;
import com.example.bookwise.domain.user.entity.User;
import com.example.bookwise.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookClickService {

    private final BookClickRepository bookClickRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookClick clickBook(Long userId, String bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        BookClick bookClick = bookClickRepository.findByUserAndBookId(user, bookId);

        if (bookClick == null) {
            bookClick = new BookClick(null, user, bookId, 1L);
        } else {
            bookClick.increase();
        }

        return bookClickRepository.save(bookClick);
    }

}
