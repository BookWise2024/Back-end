package com.example.bookwise.domain.wishcategory.repository;

import com.example.bookwise.domain.wishcategory.entity.Wishcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishcategoryRepository extends JpaRepository<Wishcategory, Long> {


    Optional<Wishcategory> findByUser_UserIdAndCategory(Long userId, String category);
    List<Wishcategory> findByUser_UserId(Long userId);
}