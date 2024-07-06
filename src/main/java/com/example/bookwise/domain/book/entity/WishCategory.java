package com.example.bookwise.domain.book.entity;

import com.example.bookwise.domain.book.Enum.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class WishCategory {
    @Id
    private String wishId;

    private Category category;

    private String count;

    private String userId;

}
