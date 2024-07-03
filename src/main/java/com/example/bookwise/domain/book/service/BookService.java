package com.example.bookwise.domain.book.service;

import com.example.bookwise.domain.book.dto.BookDetailDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BookService {

    @Value("${aladin.api.key}")
    private String apiKey;

            private final RestTemplate restTemplate;
            private final ObjectMapper objectMapper;


            public ResponseEntity<?> getBookDetails(String isbn) throws IOException {
        //
        //        // url에 api 키 와 isbn 동적으로 삽입 ( json 형식으로 받아옴 )
        String apiUrl = String.format("https://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=%s&itemIdType=ISBN&ItemId=%s&output=js&Version=20131101", apiKey, isbn);
        String response = restTemplate.getForObject(apiUrl, String.class); // 지정 url 로 get 요청

        //파싱
        JsonNode root = objectMapper.readTree(response);
        JsonNode item = root.path("item").get(0);

        BookDetailDto bookDetailDto = new BookDetailDto(
                isbn,
                item.path("cover").asText(),
                item.path("title").asText(),
                item.path("author").asText(),
                item.path("styleDesc").asText(),  // assuming you have this field in the JSON response
                item.path("pubDate").asText(),
                item.path("publisher").asText(),
                item.path("categoryName").asText(),
                "",  // subcategory is not in the original JSON response
                item.path("description").asText()
        );

        return ResponseEntity.ok(bookDetailDto);
    }
}
