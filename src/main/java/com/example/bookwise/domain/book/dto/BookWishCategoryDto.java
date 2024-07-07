package com.example.bookwise.domain.book.dto;

import com.example.bookwise.domain.wishcategory.entity.Wishcategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookWishCategoryDto {
    String category;
    Long count;

    public BookWishCategoryDto(Wishcategory wishcategory) {
        this.category = wishcategory.getCategory();
        this.count = wishcategory.getCount();
    }
}
