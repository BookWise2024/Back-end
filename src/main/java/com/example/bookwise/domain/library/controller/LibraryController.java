package com.example.bookwise.domain.library.controller;


import com.example.bookwise.domain.library.dto.*;
import com.example.bookwise.domain.library.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/library")
@Tag(name = "도서관")
public class LibraryController {

    private final LibraryService libraryService;


    // 도서관 위치조회
    @Operation(summary = "도서관 위치조회", description = "도서관 위치를 조회합니다.")
    @GetMapping
    public LibraryListResponse getLibraryList(LibraryMapDto libraryMapDto) {
        return libraryService.getLibraryByDistance(libraryMapDto);
    }


    // 도서관 상세정보 조회
    @Operation(summary = "도서관 상세정보 조회", description = "도서관의 상세정보를 조회합니다.")
    @GetMapping("/{libraryId}")
    public LibraryDetailResponse getLibraryDetail(@PathVariable long libraryId) throws Exception {
        return libraryService.getLibraryDetail(libraryId);
    }



    // 도서관 위치조회 (도서정보기반)
    @Operation(summary = "도서관 위치조회 (도서정보기반)", description = "도서정보기반으로 도서관의 위치를 조회합니다.")
    @GetMapping("/book/{bookId}")
    public LibraryListByBookResponse getLibraryListByBook(@PathVariable String bookId,LibraryMapDto libraryMapDto) throws Exception {
        return libraryService.getLibraryByBook(bookId,libraryMapDto);
    }


    // 도서관 상세정보 조회 (도서정보기반)
    @Operation(summary = "도서관 상세정보 조회 (도서정보기반)", description = "도서관과 도서의 상세정보를 조회하고 도서관에 해당 도서를 보유,대출 여부를 알려줍니다.\n해당 도서와 비슷한 도서들을 보여줍니다.")
    @GetMapping("/book/{bookId}/{libraryId}")
    public LibraryDetailByBookResponse getLibraryListByBook(@PathVariable String bookId,@PathVariable Long libraryId, @RequestParam("has-book") String hasBook, @RequestParam("loan-available") String loanAvailable) throws Exception {
        HasBookDto hasBookDto = new HasBookDto(hasBook,loanAvailable);
        return libraryService.getLibraryDetailByBook(bookId,libraryId,hasBookDto);
    }




}
