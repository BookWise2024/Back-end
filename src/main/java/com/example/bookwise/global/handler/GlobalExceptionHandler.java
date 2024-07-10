package com.example.bookwise.global.handler;

import com.example.bookwise.global.error.CustomForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomForbiddenException.class)
    public ResponseEntity<String> handleCustomForbiddenException(CustomForbiddenException ex) {
        return new ResponseEntity<>("토큰 에러입니다", HttpStatus.FORBIDDEN);
    }

    // 다른 예외 핸들러도 추가할 수 있습니다.
}