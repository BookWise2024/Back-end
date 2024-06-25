package com.example.bookwise.domain.library.repository;

import com.example.bookwise.domain.library.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LibraryRepository extends JpaRepository<Library,Long> {

}
