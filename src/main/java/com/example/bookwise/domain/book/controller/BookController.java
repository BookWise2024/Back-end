package com.example.bookwise.domain.book.controller;

import com.example.bookwise.domain.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

    @GetMapping("/{isbn}/{user_id}")
    public ResponseEntity<?> getBookDetail(@PathVariable("isbn") String isbn, @PathVariable("user_id") Long userId) throws IOException {
        return bookService.getBookDetails(isbn, userId);
    }

    @PostMapping("/{isbn}/{user_id}/like")
    public ResponseEntity<String> addWishlist(@PathVariable("user_id") Long userId, @PathVariable("isbn") String isbn) throws JsonProcessingException {
        return bookService.addWishlist(isbn, userId);
    }
    @DeleteMapping("/{isbn}/{user_id}/like")
    public ResponseEntity<String> deleteWishlist(@PathVariable("user_id") Long userId, @PathVariable("isbn") String isbn) throws JsonProcessingException {
        return bookService.deleteWishlist(isbn, userId);
    }
}
