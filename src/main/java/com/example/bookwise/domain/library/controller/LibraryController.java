package com.example.bookwise.domain.library.controller;


import com.example.bookwise.domain.library.dto.LibraryDetailResponse;
import com.example.bookwise.domain.library.dto.LibraryListByBookResponse;
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


    // 도서관 위치조회
    @GetMapping
    public LibraryListResponse getLibraryList(LibraryMapDto libraryMapDto) {
        return libraryService.getLibraryByDistance(libraryMapDto);
    }


    // 도서관 상세정보 조회
    @GetMapping("/{libraryId}")
    public LibraryDetailResponse getLibraryDetail(@PathVariable long libraryId) throws Exception {
        return libraryService.getLibraryDetail(libraryId);
    }



    // 도서관 위치조회 (도서정보기반)
    @GetMapping("/book/{bookId}")
    public LibraryListByBookResponse getLibraryListByBook(@PathVariable long bookId,LibraryMapDto libraryMapDto) throws Exception {
        return libraryService.getLibraryByBook(bookId,libraryMapDto);
    }

}
