package com.example.bookwise.domain.book.repository;
import com.example.bookwise.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    Optional<Book> findByBookId(String bookId);
}