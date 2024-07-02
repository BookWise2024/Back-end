package com.example.bookwise.domain.wishilist.service;


import com.example.bookwise.domain.wishilist.dto.WishlistBookDto;
import com.example.bookwise.domain.wishilist.dto.WishlistResponse;
import com.example.bookwise.domain.wishilist.entity.Wishlist;
import com.example.bookwise.domain.wishilist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;



    // 위시리스트 조회
    public WishlistResponse getWishlist(Long userId) {

        List<Wishlist> wishlist = wishlistRepository.findByUserUserId(userId);

        List<WishlistBookDto> result = new ArrayList<>();
        for (Wishlist list : wishlist) {
            result.add(new WishlistBookDto(list.getBook().getBookId(), list.getBook().getCoverUrl()));
        }


        return new WishlistResponse(result.size(), result);
    }


    // 위시리스트 검색
    public WishlistResponse getWishlistBySearch(Long userId, String keyword) {
        keyword = keyword.replace(" ", "");
        List<Wishlist> wishlist = wishlistRepository.findByUserUserIdAndBook_TitleContaining(userId, keyword);
        List<WishlistBookDto> result = new ArrayList<>();
        for (Wishlist list : wishlist) {
            result.add(new WishlistBookDto(list.getBook().getBookId(), list.getBook().getCoverUrl()));
        }

        return new WishlistResponse(result.size(), result);

    }


}