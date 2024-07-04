package com.example.bookwise.domain.book.service;

import com.example.bookwise.domain.book.dto.BookByMlDto;
import com.example.bookwise.domain.book.dto.BookDetailDto;
import com.example.bookwise.domain.wishcategory.entity.Wishcategory;
import com.example.bookwise.domain.wishcategory.repository.WishcategoryRepository;
import com.example.bookwise.domain.wishcategory.service.WishcategoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    @Value("${aladin.api.key}")
    private String apiKey;
    @Value("${url.ml}")
    private String url;

    private final WishcategoryRepository wishcategoryRepository;
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


    // 카페고리 높은 2개 보내고 추천 도서들 받기
    public List<BookByMlDto> getBooksByCategoryCount(Long userId) throws IOException {
        List<Wishcategory> wishcategoryList = wishcategoryRepository.findByUser_UserId(userId);

        // count 기준으로 오름차순 정렬되는 PriorityQueue 생성
        PriorityQueue<Wishcategory> priorityQueue = new PriorityQueue<>(2, (w1, w2) -> Long.compare(w1.getCount(), w2.getCount()));

        for (Wishcategory wishcategory : wishcategoryList) {
            priorityQueue.offer(wishcategory);
            if (priorityQueue.size() > 2) {
                priorityQueue.poll(); // 최소 값을 제거하여 상위 2개의 요소만 유지
            }
        }

        // ML 서버 URL 설정
        String urlStr = UriComponentsBuilder.fromHttpUrl(url)
                .path("/api/recommend/wishlist/count")
                .queryParam("preferred_cateogries", priorityQueue.poll())
                .queryParam("preferred_cateogries", priorityQueue.poll())
                .toUriString();
        String response = restTemplate.getForObject(urlStr, String.class);
        log.info(response);
        return parseJsonResponse(response);
    }

    // isbn 보내고 비슷한 도서들 받기
    public List<BookByMlDto> getSimilarBook(String bookId) throws IOException {

        String id = "9788934921318";
        // ML 서버 URL 설정
        String urlStr = UriComponentsBuilder.fromHttpUrl(url)
                .path("/api/recommend/similar/" + id)
                .toUriString();

        String response = restTemplate.getForObject(urlStr, String.class);
        log.info(response);
        return parseJsonResponse(response);
    }


    // 받은 도서(isbn,coverUrl) 변환
    public List<BookByMlDto> parseJsonResponse(String response) throws IOException {
        List<BookByMlDto> bookList = new ArrayList<>();

        bookList = objectMapper.readValue(response, new TypeReference<List<BookByMlDto>>() {
        });

        return bookList;
    }


}
