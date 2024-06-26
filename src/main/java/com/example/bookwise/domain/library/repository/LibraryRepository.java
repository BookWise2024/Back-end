package com.example.bookwise.domain.library.repository;

import com.example.bookwise.domain.library.dto.LibraryDetailResponse;
import com.example.bookwise.domain.library.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {


    @Query(value = "SELECT * FROM Library l WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(l.latitude)) * cos(radians(l.longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(l.latitude)))) <= :ran", nativeQuery = true)
    List<Library> findLibrariesWithinDistance(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("ran") double range
    );

    Optional<Library> findByLibraryId(Long libraryId);
}