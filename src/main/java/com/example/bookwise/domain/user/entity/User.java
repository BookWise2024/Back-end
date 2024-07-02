package com.example.bookwise.domain.user.entity;


import com.example.bookwise.domain.wishilist.entity.Wishlist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Wishlist> wishlist = new ArrayList<>();

    public User(Long userId,String email) {
        this.userId = userId;
        this.email = email;
    }
}
