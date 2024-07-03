package com.example.bookwise.domain.wishilist.controller;



import com.example.bookwise.domain.book.entity.Book;
import com.example.bookwise.domain.book.repository.BookRepository;
import com.example.bookwise.domain.user.entity.User;
import com.example.bookwise.domain.user.repository.UserRepository;
import com.example.bookwise.domain.wishcategory.repository.WishcategoryRepository;
import com.example.bookwise.domain.wishcategory.service.WishcategoryService;
import com.example.bookwise.domain.wishilist.dto.WishlistResponse;
import com.example.bookwise.domain.wishilist.entity.Wishlist;
import com.example.bookwise.domain.wishilist.entity.WishlistId;
import com.example.bookwise.domain.wishilist.repository.WishlistRepository;
import com.example.bookwise.domain.wishilist.service.WishlistService;
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
public class WishlistController {



    private final WishlistService wishlistService;

    // 삭제예정
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final WishlistRepository wishlistRepository;
    private final WishcategoryService wishcategoryService;

    // 아이디는 로그인 구현 이후에 변경 //
    @GetMapping
    public WishlistResponse getLibraryList() {

        Long userId = 1L;
        return wishlistService.getWishlist(userId);
    }


    // 아이디는 로그인 구현 이후에 변경 //
    @GetMapping("/search")
    public WishlistResponse getWishlistBySearch(Long userId, String keyword) {
        return wishlistService.getWishlistBySearch(userId,keyword);
    }



    // 추후 삭제예정
    @PostMapping("/dummydb")
    public ResponseEntity<String> createDummyDB() {

        // 유저 생성
        User user = new User(1L,"aaa@naver.com");
        User user1 = new User(2L,"aaa@naver.com");
        userRepository.save(user);
        userRepository.save(user1);


        User u = userRepository.findByUserId(1L).orElseThrow();
        User u1 = userRepository.findByUserId(2L).orElseThrow();

        // 위시 카테고리 count 생성
        wishcategoryService.createWishcategory(1L);
        wishcategoryService.createWishcategory(2L);



        // 더미 책 db에 넣기
        for(int i =0;i<=9;i++) {
            Book book = new Book("978893783089"+i,"https://image.aladin.co.kr/product/58/20/coversum/8937830892_2.jpg","[중고] 밤의 피크닉","온다 리쿠 (지은이), 권남희 (옮긴이)","?몰라?","2005-09-05","북폴리오","소설/시/희곡","일본소설","2005년 제26회 요시카와 에이지 문학상 신인상 수상작. 일본 서점 직원들이 선정하는 제2회 서점대상 수상작이기도 하다. 1회 수상작은 국내에도 소개되어 많은 사랑을 받은 . 어린 날의 떨림과 반짝거림, 가볍게 들떠 있다가도 곧 무겁게 가라앉곤 하는 10대 시절의 공기를 예리하게 그려냈다.");
            Book book1 = new Book("978893783088"+i,"https://image.aladin.co.kr/product/58/20/coversum/8937830892_2.jpg","[중고] 밤의 댄스","온다 리쿠 (지은이), 권남희 (옮긴이)","?몰라?","2005-09-05","북폴리오","소설/시/희곡","일본소설","2005년 제26회 요시카와 에이지 문학상 신인상 수상작. 일본 서점 직원들이 선정하는 제2회 서점대상 수상작이기도 하다. 1회 수상작은 국내에도 소개되어 많은 사랑을 받은 . 어린 날의 떨림과 반짝거림, 가볍게 들떠 있다가도 곧 무겁게 가라앉곤 하는 10대 시절의 공기를 예리하게 그려냈다.");

            bookRepository.save(book);
            bookRepository.save(book1);
        }



        // 위시리스트에 책 넣기
        for(int i =0;i<=9;i++) {
            Book b =bookRepository.findByBookId("978893783089"+i).orElseThrow();
            Book b1 =bookRepository.findByBookId("978893783088"+i).orElseThrow();

            Wishlist wishlist1 = new Wishlist(new WishlistId(u.getUserId(),b.getBookId()),u,b);
            Wishlist wishlist2 = new Wishlist(new WishlistId(u.getUserId(),b1.getBookId()),u,b1);
            Wishlist wishlist3 = new Wishlist(new WishlistId(u1.getUserId(),b.getBookId()),u1,b);
            Wishlist wishlist4 = new Wishlist(new WishlistId(u1.getUserId(),b1.getBookId()),u1,b1);

            wishlistRepository.save(wishlist1);
            wishlistRepository.save(wishlist2);
            wishlistRepository.save(wishlist3);
            wishlistRepository.save(wishlist4);

        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }





}
