package com.example.bookwise.domain.book.dto;


import com.example.bookwise.domain.bookclick.entity.BookClick;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookClickDto {
    String bookId;
    Long click;

    public BookClickDto(BookClick bookClick1) {
        this.bookId = bookClick1.getBookId();
        this.click = bookClick1.getClick();
    }
}
