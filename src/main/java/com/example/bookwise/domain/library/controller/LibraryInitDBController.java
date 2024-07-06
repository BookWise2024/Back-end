package com.example.bookwise.domain.library.controller;


import com.example.bookwise.domain.library.service.LibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class LibraryInitDBController implements ApplicationRunner {
    @Autowired
    private LibraryService libraryService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String status = libraryService.createLibraryInitDB();
        log.info("status={}", status);

    }


}
