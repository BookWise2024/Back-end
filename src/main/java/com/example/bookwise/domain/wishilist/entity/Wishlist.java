package com.example.bookwise.domain.wishilist.entity;

import com.example.bookwise.domain.book.entity.Book;
import com.example.bookwise.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Wishlist {

    @EmbeddedId
    private WishlistId wishlistId;

    @MapsId("userId")
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("bookId")
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="book_id")
    private Book book;

    @CreatedDate
    private LocalDateTime createAt;   // 주문시간


    public Wishlist(WishlistId wishlistId,User user,Book book) {
        this.wishlistId =wishlistId;
        this.user = user;
        this.book = book;

    }

    public Wishlist(User user, Book book1) {
        this.book = book1;
        this.user = user;
        this.wishlistId = new WishlistId(user.getUserId(), book1.getBookId());
    }
}
