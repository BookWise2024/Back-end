package com.example.bookwise.domain.wishilist.repository;

import com.example.bookwise.domain.book.entity.Book;
import com.example.bookwise.domain.user.entity.User;
import com.example.bookwise.domain.wishilist.dto.WishlistBookDto;
import com.example.bookwise.domain.wishilist.entity.Wishlist;
import com.example.bookwise.domain.wishilist.entity.WishlistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, WishlistId> {




    List<Wishlist> findByUserUserId(Long userId);

    List<Wishlist> findByUserUserIdAndBook_TitleContaining(Long userId, String keyword);

    Optional<Wishlist> findByUserAndBook(User user, Book book);
}