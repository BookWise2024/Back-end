package com.example.bookwise.domain.initdb.controller;


import com.example.bookwise.domain.initdb.service.InitDbService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/init")
@Tag(name = "초기 데이터 저장")
public class InitDbController {

    private final InitDbService initDbService;


    @Operation(summary = "도서관 데이터 저장", description = "공공 도서관 데이터들을 저장합니다.")
    @PostMapping("/library")
    public String createLibraryInitDB() throws Exception {
        return initDbService.createLibraryInitDB();
    }

    @Operation(summary = "도서관 데이터 저장", description = "공공 도서관 데이터들을 저장합니다.")
    @PostMapping("/book")
    public String createBookInitDB() throws Exception {
        return initDbService.createBookInitDB();
    }

}
