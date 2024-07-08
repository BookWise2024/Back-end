package com.example.bookwise.domain.wishilist.controller;



import com.example.bookwise.domain.book.entity.Book;
import com.example.bookwise.domain.book.repository.BookRepository;
import com.example.bookwise.domain.user.entity.User;
import com.example.bookwise.domain.user.repository.UserRepository;
import com.example.bookwise.domain.wishcategory.repository.WishcategoryRepository;
import com.example.bookwise.domain.wishcategory.service.WishcategoryService;
import com.example.bookwise.domain.wishilist.dto.WishlistBookDto;
import com.example.bookwise.domain.wishilist.dto.WishlistResponse;
import com.example.bookwise.domain.wishilist.entity.Wishlist;
import com.example.bookwise.domain.wishilist.entity.WishlistId;
import com.example.bookwise.domain.wishilist.repository.WishlistRepository;
import com.example.bookwise.domain.wishilist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
@Tag(name = "위시리스트")
public class WishlistController {

    private final WishlistService wishlistService;

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;




    // 아이디는 로그인 구현 이후에 변경 //
    @Operation(summary = "위시리스트 조회", description = "유저 위시리스트의 도서들을 조회합니다.")
    @GetMapping
    public WishlistResponse getLibraryList(@RequestHeader("Authorization") String accessToken) throws Exception {

//        Long userId = 1L;


        return wishlistService.getWishlist(accessToken);
    }



    // 아이디는 로그인 구현 이후에 변경 //
    @Operation(summary = "위시리스트 검색", description = "키워드로 검색하면 일치하는 유저 위시리스트의 도서들을 조회합니다.")
    @GetMapping("/search")
    public WishlistResponse getWishlistBySearch(@RequestHeader("Authorization") String accessToken, String keyword) throws Exception {
        return wishlistService.getWishlistBySearch(accessToken,keyword);
    }



//    // 추후 삭제예정
//    @PostMapping("/dummydb")
//    public ResponseEntity<String> createDummyDB() {
//
//        User user = new User(1L,"aa@naver.com","nickname");
//
//        userRepository.save(user);
//
//        // 더미 책 db에 넣기
//        for(int i =0;i<=9;i++) {
//            Book book = new Book("978893783089"+i,"https://image.aladin.co.kr/product/58/20/coversum/8937830892_2.jpg","[중고] 밤의 피크닉","온다 리쿠 (지은이), 권남희 (옮긴이)","?몰라?","2005-09-05","북폴리오","소설/시/희곡","일본소설","2005년 제26회 요시카와 에이지 문학상 신인상 수상작. 일본 서점 직원들이 선정하는 제2회 서점대상 수상작이기도 하다. 1회 수상작은 국내에도 소개되어 많은 사랑을 받은 . 어린 날의 떨림과 반짝거림, 가볍게 들떠 있다가도 곧 무겁게 가라앉곤 하는 10대 시절의 공기를 예리하게 그려냈다.");
//            Book book1 = new Book("978893783088"+i,"https://image.aladin.co.kr/product/58/20/coversum/8937830892_2.jpg","[중고] 밤의 댄스","온다 리쿠 (지은이), 권남희 (옮긴이)","?몰라?","2005-09-05","북폴리오","소설/시/희곡","일본소설","2005년 제26회 요시카와 에이지 문학상 신인상 수상작. 일본 서점 직원들이 선정하는 제2회 서점대상 수상작이기도 하다. 1회 수상작은 국내에도 소개되어 많은 사랑을 받은 . 어린 날의 떨림과 반짝거림, 가볍게 들떠 있다가도 곧 무겁게 가라앉곤 하는 10대 시절의 공기를 예리하게 그려냈다.");
//
//            bookRepository.save(book);
//            bookRepository.save(book1);
//        }
//
//        Book book = new Book("9788925813691","https://image.aladin.co.kr/product/58/20/coversum/8937830892_2.jpg","테스트!!!!","온다 리쿠 (지은이), 권남희 (옮긴이)","?몰라?","2005-09-05","북폴리오","소설/시/희곡","일본소설","2005년 제26회 요시카와 에이지 문학상 신인상 수상작. 일본 서점 직원들이 선정하는 제2회 서점대상 수상작이기도 하다. 1회 수상작은 국내에도 소개되어 많은 사랑을 받은 . 어린 날의 떨림과 반짝거림, 가볍게 들떠 있다가도 곧 무겁게 가라앉곤 하는 10대 시절의 공기를 예리하게 그려냈다.");
//
//
//        bookRepository.save(book);
//
//
//        User user1 =  userRepository.findByUserId(1L).orElseThrow();
//        for(int i =0;i<=9;i++) {
//            Book b = bookRepository.getReferenceById("978893783089"+i);
//            Wishlist wishlist = new Wishlist(new WishlistId(user1.getUserId(),b.getBookId()),user1,b);
//
//            wishlistRepository.save(wishlist);
//
//        }

//        return new ResponseEntity<>(HttpStatus.ACCEPTED);
//    }





}
