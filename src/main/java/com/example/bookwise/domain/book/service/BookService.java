package com.example.bookwise.domain.book.service;

import com.example.bookwise.domain.book.Enum.Category;
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

        // url에 api 키 와 isbn 동적으로 삽입 ( json 형식으로 받아옴 )
        String apiUrl = String.format("https://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=%s&itemIdType=ISBN&ItemId=%s&output=js&Version=20131101&SearchTarget=Book", apiKey, isbn);
        String response = restTemplate.getForObject(apiUrl, String.class); // 지정 url 로 get 요청

        //파싱
        JsonNode root = objectMapper.readTree(response);
        JsonNode item = root.path("item").get(0);
        // 상세 책 검색하거나 좋다고 누르면 카테고리에 + 1 해줘야해

        // 카테고리 분리 로직
        String category = item.path("categoryName").asText();
        String mainCategory = "";
        String subCategory = "";

        // 상하위 카테고리 분리
        if (category.contains(">")) {
            String[] categoryParts = category.split(">");
            if (categoryParts.length > 1) {
                mainCategory = categoryParts[1].trim();
                subCategory = categoryParts[2].trim();
            }
        }

        String itemId = item.path("itemId").asText();
        BookDetailDto bookDetailDto = new BookDetailDto(
                isbn,
                item.path("cover").asText(),
                item.path("title").asText(),
                item.path("author").asText(),
                item.path("SearchTarget").asText(),  //searchtarget 과 동일하다
                item.path("pubDate").asText(),
                item.path("publisher").asText(),
                mainCategory,
                subCategory,
                item.path("description").asText(),
                itemId
        );

        sendItemIdToDataTeam(itemId);

        return ResponseEntity.ok(bookDetailDto);
    }

    private void sendItemIdToDataTeam(String itemId) {
        String dataTeamApiUrl = String.format("http://13.125.215.49:8000/api/sentiment/reviews/%s", itemId);
        restTemplate.postForObject(dataTeamApiUrl, null, String.class);
    }
}
