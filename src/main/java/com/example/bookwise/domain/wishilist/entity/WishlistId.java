package com.example.bookwise.domain.wishilist.entity;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class WishlistId implements Serializable {
    private Long userId;
    private String bookId;


}