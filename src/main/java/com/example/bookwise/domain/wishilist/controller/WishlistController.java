package com.example.bookwise.domain.wishilist.controller;



import com.example.bookwise.domain.wishilist.dto.WishlistResponse;
import com.example.bookwise.domain.wishilist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
@Tag(name = "위시리스트")
public class WishlistController {

    private final WishlistService wishlistService;




    // 아이디는 로그인 구현 이후에 변경 //
    @Operation(summary = "위시리스트 조회", description = "유저 위시리스트의 도서들을 조회합니다.")
    @GetMapping
    public WishlistResponse getWishlist(@RequestHeader("Authorization") String accessToken) throws Exception {


        return wishlistService.getWishlist(accessToken);
    }



    // 아이디는 로그인 구현 이후에 변경 //
    @Operation(summary = "위시리스트 검색", description = "키워드로 검색하면 일치하는 유저 위시리스트의 도서들을 조회합니다.")
    @GetMapping("/search")
    public WishlistResponse getWishlistBySearch(@RequestHeader("Authorization") String accessToken, String keyword) throws Exception {
        return wishlistService.getWishlistBySearch(accessToken,keyword);
    }



//
//    @GetMapping("/book/{isbn}")
//    public WishlistIsExistResponse getLibraryList(@RequestHeader("Authorization") String accessToken, @PathVariable("isbn") String isbn) throws Exception {
//
//        return wishlistService.getWishlistByBook(accessToken,isbn);
//    }




}
