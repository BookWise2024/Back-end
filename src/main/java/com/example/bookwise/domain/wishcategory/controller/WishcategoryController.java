package com.example.bookwise.domain.wishcategory.controller;

import com.example.bookwise.domain.book.service.BookService;
import com.example.bookwise.domain.wishcategory.entity.Wishcategory;
import com.example.bookwise.domain.wishcategory.service.WishcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishcategory")
public class WishcategoryController {

    private final WishcategoryService wishcategoryService;


    // 테스트용 삭제예정
    @GetMapping("/inreasment")
    public ResponseEntity<?> increaseWishcategory() throws IOException {
        return wishcategoryService.increaseWishcategory(1L,"소설/시/희곡");
    }

    // 테스트용 삭제예정
    @GetMapping("/decreasment")
    public ResponseEntity<?> decreaseWishcategory() throws IOException {
        return wishcategoryService.decreaseWishcategory(1L,"소설/시/희곡");
    }
}
