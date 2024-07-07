package com.example.bookwise.domain.book.service;

import com.example.bookwise.domain.book.dto.*;
import com.example.bookwise.domain.book.entity.Book;
import com.example.bookwise.domain.book.repository.BookRepository;
import com.example.bookwise.domain.bookclick.entity.BookClick;
import com.example.bookwise.domain.bookclick.repository.BookClickRepository;
import com.example.bookwise.domain.bookclick.service.BookClickService;
import com.example.bookwise.domain.user.entity.User;
import com.example.bookwise.domain.user.repository.UserRepository;
import com.example.bookwise.domain.wishcategory.entity.Wishcategory;
import com.example.bookwise.domain.wishcategory.repository.WishcategoryRepository;
import com.example.bookwise.domain.wishcategory.service.WishcategoryService;
import com.example.bookwise.domain.wishilist.entity.Wishlist;
import com.example.bookwise.domain.wishilist.repository.WishlistRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    private final BookClickService bookClickService;
    private final WishcategoryRepository wishcategoryRepository;
    private final WishcategoryService wishcategoryService;
    private final BookClickRepository bookClickRepository;
    private final WishlistRepository wishlistRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ResponseEntity<?> getBookDetails(String isbn, Long userId) throws IOException {

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

//       // sendItemIdToDataTeam(itemId);
//        // 상세조회시 위시 카테고리 up
//        wishcategoryService.increaseWishcategory(userId,mainCategory);
//        // 클릭 수 up
//        bookClickService.clickBook(userId, isbn);

        return ResponseEntity.ok(bookDetailDto);
    }

    private void sendItemIdToDataTeam(String itemId) {
        String dataTeamApiUrl = String.format("http://13.125.215.49:5000/api/sentiment/reviews/%s", itemId);
        restTemplate.postForObject(dataTeamApiUrl, null, String.class);
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
    // 즐겨찾기

    @Transactional
    public ResponseEntity<String> addWishlist(String isbn, Long userId) throws JsonProcessingException {
        // 유저를 찾고, 없으면 예외 발생
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저는 존재하지 않습니다."));
        System.out.println(user.getUserId());

        // 책을 찾고, 없으면 예외 발생
        Book optionalBook = bookRepository.findByBookId(isbn).orElseThrow(() -> new EntityNotFoundException("해당 책은 존재하지 않습니다."));
        System.out.println(optionalBook.getBookId());
        Book book;

        if (optionalBook != null) {
            book = optionalBook;
        } else {
            book = new Book(findBookAladin(isbn));
            bookRepository.save(book);
        }
        // 위시리스트 생성
        Wishlist wishlist = new Wishlist(user, book);
        wishlistRepository.save(wishlist); // 위시리스트 저장

        return ResponseEntity.ok("위시리스트에 추가되었습니다.");
    }
    public ResponseEntity<String> deleteWishlist(String isbn, Long userId) throws JsonProcessingException {
        // 유저를 찾고, 없으면 예외 발생
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저는 존재하지 않습니다."));

        // 책을 찾고, 없으면 예외 발생
        Book book = bookRepository.findByBookId(isbn)
                .orElseThrow(() -> new EntityNotFoundException("해당 책 정보는 존재하지 않습니다."));

        // 위시리스트에서 해당 유저와 책 정보를 찾고 삭제
        Wishlist wishlist = wishlistRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new EntityNotFoundException("해당 위시리스트 항목은 존재하지 않습니다."));

        wishlistRepository.delete(wishlist);
        return ResponseEntity.ok("위시리스트에 삭제되었습니다.");
    }


    private BookDetailDto findBookAladin(String isbn) throws JsonProcessingException {
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
        return bookDetailDto;
    }

    public ResponseEntity<?> getBookList(Long userId) {

       List<Wishlist> wishlists = wishlistRepository.findByUserUserId(userId);
       ArrayList<String> bookList = new ArrayList<>();

       for(Wishlist wishlist : wishlists){
           bookList.add(wishlist.getBook().getBookId());
       }


       List<BookWishCategoryDto> bookWishCategoryDtos = new ArrayList<>();
       List<Wishcategory> wishCateorys = wishcategoryRepository.findByUser_UserId(userId);
       for(Wishcategory wishcategory : wishCateorys){
           BookWishCategoryDto bw = new BookWishCategoryDto(wishcategory);
          bookWishCategoryDtos.add(bw);
       }


       List<BookClickDto> bookClickDtos = new ArrayList<>();
       List<BookClick> bookClick = bookClickRepository.findByUserUserId(userId);

       for(BookClick bookClick1 : bookClick){
           BookClickDto bt = new BookClickDto(bookClick1);
           bookClickDtos.add(bt);
       }

       // 유저 선호책, 카레고리, 책별 클릭수
        BookRecommendDto br = new BookRecommendDto(bookList, bookWishCategoryDtos, bookClickDtos );

        // ML 서버 URL 설정
        String urlStr = UriComponentsBuilder.fromHttpUrl(url)
                .path("/api/recommend/recommendations")
                .toUriString();


      //  ResponseEntity<String> response = restTemplate.postForEntity(urlStr, br, String.class);
      //  log.info(String.valueOf(response));
        // 응답 반환
     //   return ResponseEntity.ok(response.getBody());
        return ResponseEntity.ok(br);
    }
}
