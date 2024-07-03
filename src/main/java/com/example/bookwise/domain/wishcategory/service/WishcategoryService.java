package com.example.bookwise.domain.wishcategory.service;

import com.example.bookwise.domain.user.entity.User;
import com.example.bookwise.domain.user.repository.UserRepository;
import com.example.bookwise.domain.wishcategory.entity.Wishcategory;
import com.example.bookwise.domain.wishcategory.repository.WishcategoryRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishcategoryService {

    @Value("${aladin.api.key}")
    private String apiKey;

    private final WishcategoryRepository wishcategoryRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    // 위시카테고리생성
    @Transactional
    public ResponseEntity<String> createWishcategory(Long userId) {
        List<String> categories = Arrays.asList(
                "건강/취미", "경제경영", "공무원 수험서", "과학", "달력/기타",
                "대학교재", "만화", "사회과학", "소설/시/희곡", "수험서/자격증",
                "어린이", "에세이", "여행", "역사", "예술/대중문화",
                "외국어", "요리/살림", "유아", "인문학", "자기계발",
                "잡지", "장르소설", "전집/중고전집", "종교/역학", "좋은부모",
                "청소년", "컴퓨터/모바일", "초등학교참고서", "중학교참고서", "고등학교참고서"
        );

        //
        User user = userRepository.findByUserId(userId).orElseThrow();
        for (String category : categories) {
            Wishcategory wishcategory = new Wishcategory(category,user);

            wishcategoryRepository.save(wishcategory);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    // 위시카테고리 증가
    @Transactional
    public ResponseEntity<String> increaseWishcategory(Long userId,String category) {

        Wishcategory wishcategory = wishcategoryRepository.findByUser_UserIdAndCategory(userId,category).orElseThrow();

        wishcategory.increase();

        wishcategoryRepository.save(wishcategory);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }



    // 위시카테고리 감소
    @Transactional
    public ResponseEntity<String> decreaseWishcategory(Long userId,String category) {

        Wishcategory wishcategory = wishcategoryRepository.findByUser_UserIdAndCategory(userId,category).orElseThrow();

        wishcategory.decrease();

        wishcategoryRepository.save(wishcategory);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }




}
