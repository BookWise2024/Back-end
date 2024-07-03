package com.example.bookwise.domain.wishcategory.controller;

import com.example.bookwise.domain.book.dto.BookByMlDto;
import com.example.bookwise.domain.book.service.BookService;
import com.example.bookwise.domain.library.dto.HasBookDto;
import com.example.bookwise.domain.library.dto.LibraryDetailByBookResponse;
import com.example.bookwise.domain.wishcategory.entity.Wishcategory;
import com.example.bookwise.domain.wishcategory.service.WishcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishcategory")
public class WishcategoryController {

    private final WishcategoryService wishcategoryService;


    // 테스트용 삭제예정
    @GetMapping("/inreasment")
    public ResponseEntity<?> increaseWishcategory() throws IOException {
        wishcategoryService.increaseWishcategory(1L,"과학");
        wishcategoryService.increaseWishcategory(1L,"과학");
        wishcategoryService.increaseWishcategory(1L,"과학");
        wishcategoryService.increaseWishcategory(1L,"만화");
        wishcategoryService.increaseWishcategory(1L,"만화");


        return wishcategoryService.increaseWishcategory(1L,"소설/시/희곡");
    }

    // 테스트용 삭제예정
    @GetMapping("/decreasment")
    public ResponseEntity<?> decreaseWishcategory() throws IOException {
        return wishcategoryService.decreaseWishcategory(1L,"소설/시/희곡");
    }



    // ML 받는거 테스트
    @GetMapping("/test")
    public List<BookByMlDto> getLibraryListByBook() throws Exception {
        Long userId = 1L;
        return wishcategoryService.getBooksByCategoryCount(userId);
    }


    // ML 받는거 테스트2
    @GetMapping("/test2")
    public List<BookByMlDto> getSimilarBook() throws Exception {
        Long userId = 1L;

        // 연애의행방
        return wishcategoryService.getSimilarBook("9791163890119");
    }


    // ML 받는거 테스트
    @GetMapping("/test1")
    public List<BookByMlDto> getLibraryListByBooktest() throws Exception {
        Long userId = 1L;
        String str = "{\n" +
                "    \"9789876543210\": \"https://example.com/cover31.jpg\",\n" +
                "    \"9782345678901\": \"https://example.com/cover32.jpg\",\n" +
                "    \"9783456789012\": \"https://example.com/cover33.jpg\"\n" +
                "}";

        return wishcategoryService.parseJsonResponse(str);
    }


}
