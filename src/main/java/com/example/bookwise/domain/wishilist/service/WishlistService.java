package com.example.bookwise.domain.wishilist.service;


import com.example.bookwise.domain.book.entity.Book;
import com.example.bookwise.domain.book.repository.BookRepository;
import com.example.bookwise.domain.oauth.jwt.JwtTokenProvider;
import com.example.bookwise.domain.user.entity.User;
import com.example.bookwise.domain.user.repository.UserRepository;
import com.example.bookwise.domain.wishilist.dto.WishlistBookDto;
import com.example.bookwise.domain.wishilist.dto.WishlistResponse;
import com.example.bookwise.domain.wishilist.entity.Wishlist;
import com.example.bookwise.domain.wishilist.entity.WishlistId;
import com.example.bookwise.domain.wishilist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;



    // 위시리스트 조회
    public WishlistResponse getWishlist(String accessToken) {
        Long userId = jwtTokenProvider.extractId(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow();

        System.out.println(userId+" // "+user.getUserId());

        List<Wishlist> wishlist = wishlistRepository.findByUserUserId(user.getUserId());

        List<WishlistBookDto> result = new ArrayList<>();
        for (Wishlist list : wishlist) {
            result.add(new WishlistBookDto(list.getBook().getBookId(), list.getBook().getCoverUrl()));
        }

        return new WishlistResponse(result.size(), result);
    }

    // 위시리스트 검색
    public WishlistResponse getWishlistBySearch(Long userId, String keyword) {
        keyword = keyword.trim();

        List<Wishlist> wishlist = wishlistRepository.findByUserUserIdAndBook_TitleContaining(userId, keyword);
        List<WishlistBookDto> result = new ArrayList<>();
        for (Wishlist list : wishlist) {
            result.add(new WishlistBookDto(list.getBook().getBookId(), list.getBook().getCoverUrl()));
        }

        return new WishlistResponse(result.size(), result);

    }


}