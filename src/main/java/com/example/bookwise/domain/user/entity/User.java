package com.example.bookwise.domain.user.entity;


import com.example.bookwise.domain.wishcategory.entity.Wishcategory;
import com.example.bookwise.domain.wishcategory.repository.WishcategoryRepository;
import com.example.bookwise.domain.wishilist.entity.Wishlist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;
    private String nickname;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Wishlist> wishlist = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Wishcategory> wishcategoryList = new ArrayList<>();

    public User(Long userId,String email,String nickname) {
        this.userId = userId;
        this.email = email;
        this.nickname =nickname;
    }
}
