package com.example.bookwise.domain.library.controller;


import com.example.bookwise.domain.library.dto.LibraryDetailResponse;
import com.example.bookwise.domain.library.dto.LibraryListResponse;
import com.example.bookwise.domain.library.dto.LibraryMapDto;
import com.example.bookwise.domain.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/library")
public class LibraryController {

    private final LibraryService libraryService;


    // 위치기반 도서관 정보조회
    @GetMapping
    public LibraryListResponse getLibraryList(LibraryMapDto libraryMapDto) {
        return libraryService.getLibraryByDistance(libraryMapDto);
    }


    // 도서관 상세정보 조회
    @GetMapping("/{libraryId}")
    public LibraryDetailResponse getLibraryDetail(@PathVariable long libraryId) throws Exception {
        return libraryService.getLibraryDetail(libraryId);
    }

}
