package com.example.bookwise.domain.book.controller;

import com.example.bookwise.domain.book.dto.BookByMlDto;
import com.example.bookwise.domain.book.service.BookService;
import com.example.bookwise.domain.oauth.jwt.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;
    private final JwtTokenProvider jwtTokenProvider;





    @GetMapping("/{isbn}")
    public ResponseEntity<?> getBookDetail(@RequestHeader("Authorization") String accessToken,@PathVariable("isbn") String isbn) throws Exception {
        Long userId;
        if(!accessToken.equals("")) {
             userId = jwtTokenProvider.extractId(accessToken);
        } else {
            userId = -1L;
        }
        return bookService.getBookDetails(isbn, userId);
    }

    @PostMapping("/{isbn}/like")
    public ResponseEntity<String> addWishlist(@RequestHeader("Authorization") String accessToken, @PathVariable("isbn") String isbn) throws JsonProcessingException {
        Long userId = jwtTokenProvider.extractId(accessToken);
        return bookService.addWishlist(isbn, userId);
    }
    @DeleteMapping("/{isbn}/like")
    public ResponseEntity<String> deleteWishlist(@RequestHeader("Authorization") String accessToken, @PathVariable("isbn") String isbn) throws JsonProcessingException {
        Long userId = jwtTokenProvider.extractId(accessToken);

        return bookService.deleteWishlist(isbn, userId);
    }

    @Operation(summary = "유저 기반 추천 도서")
    @GetMapping("/recommendations")
    public ResponseEntity<?> getBookList(@RequestHeader("Authorization") String accessToken) throws IOException {

        Long userId = jwtTokenProvider.extractId(accessToken);



        return bookService.getBookList(userId);
    }

    @Operation(summary = "비슷한 도서")
    @GetMapping("/similar/{isbn}")
    public ResponseEntity<?> getSimilarBookList(@PathVariable("isbn") String isbn) throws IOException {
        return bookService.getSimilarBook(isbn);
    }


    @Operation(summary = "리뷰데이터")
    @GetMapping("/review/{item}")
    public ResponseEntity<?> getReviewData(@PathVariable("item") String itemId) throws IOException {
        return bookService.getItemIdToDataTeam(itemId);
    }



}
