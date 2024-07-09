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
import com.example.bookwise.domain.wishilist.dto.WishlistIsExistDto;
import com.example.bookwise.domain.wishilist.entity.Wishlist;
import com.example.bookwise.domain.wishilist.repository.WishlistRepository;
import com.example.bookwise.domain.wishilist.service.WishlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.info.Info;
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
import java.util.*;

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
    private final WishlistService wishlistService;
    private final BookClickRepository bookClickRepository;
    private final WishlistRepository wishlistRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ResponseEntity<?> getBookDetails(String isbn, Long userId) throws Exception {


        Optional<Book> isBook = bookRepository.findByBookId(isbn);

        Book book = null;

        if (isBook.isPresent()) {         // db에 존재하면 그냥 return
            book = isBook.get();

        } else {                        // db에 없으면 알라딘에서 가져오기

            book = new Book(findBookAladin(isbn));
            bookRepository.save(book);
        }


        WishlistIsExistDto wishlistIsExistDto = wishlistService.getWishlistByBook(userId,isbn);

        // 책 클릭수 증가
        if(userId != -1L) {
            bookClickService.clickBook(userId, isbn);
//            wishlistIsExistDto = new WishlistIsExistDto("N");
        }

        BookDetailDto bookDetailDto = new BookDetailDto(book.getBookId(),book.getCoverUrl(),book.getTitle(),book.getAuthor(),book.getStyleDesc(),book.getPublishDate(),book.getPublisher(),book.getCategory(), book.getSubcategory(),book.getDescription(),book.getItemId(),wishlistIsExistDto.getWishlistExist());


//            // url에 api 키 와 isbn 동적으로 삽입 ( json 형식으로 받아옴 )
//            String apiUrl = String.format("https://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=%s&itemIdType=ISBN&ItemId=%s&output=js&Version=20131101&SearchTarget=Book", apiKey, isbn);
//            String response = restTemplate.getForObject(apiUrl, String.class); // 지정 url 로 get 요청
//
//            //파싱
//            JsonNode root = objectMapper.readTree(response);
//            JsonNode item = root.path("item").get(0);
//
//            // 상세 책 검색하거나 좋다고 누르면 카테고리에 + 1 해줘야해
//
//            // 카테고리 분리 로직
//            String category = item.path("categoryName").asText();
//            String mainCategory = "";
//            String subCategory = "";
//
//            // 상하위 카테고리 분리
//            if (category.contains(">")) {
//                String[] categoryParts = category.split(">");
//                if (categoryParts.length > 1) {
//                    mainCategory = categoryParts[1].trim();
//                    subCategory = categoryParts[2].trim();
//                }
//            }
//
//            String itemId = item.path("itemId").asText();
//            BookDetailDto bookDetailDto = new BookDetailDto(
//                    isbn,
//                    item.path("cover").asText(),
//                    item.path("title").asText(),
//                    item.path("author").asText(),
//                    item.path("SearchTarget").asText(),  //searchtarget 과 동일하다
//                    item.path("pubDate").asText(),
//                    item.path("publisher").asText(),
//                    mainCategory,
//                    subCategory,
//                    item.path("description").asText(),
//                    itemId
//            );


            // sendItemIdToDataTeam(itemId);
//        // 상세조회시 위시 카테고리 up
//        wishcategoryService.increaseWishcategory(userId, mainCategory);
            // 클릭 수 up

            return ResponseEntity.ok(bookDetailDto);
        }

        // 리뷰데이터 받기
        public ResponseEntity<?> getItemIdToDataTeam (String bookId) throws JsonProcessingException {

            Optional<Book> isBook = bookRepository.findByBookId(bookId);

            Book book = null;

            if (isBook.isPresent()) {         // db에 존재하면 그냥 return
                book = isBook.get();

            } else {                        // db에 없으면 알라딘에서 가져오기
                book = new Book(findBookAladin(bookId));
            }



            String dataTeamApiUrl = String.format(url+"/api/sentiment/reviews/%s", book.getItemId());
            //      String response  = restTemplate.postForObject(dataTeamApiUrl, null, String.class);
            String response = restTemplate.getForObject(dataTeamApiUrl, String.class);

            BookReviewDto list = objectMapper.readValue(response, BookReviewDto.class);
            return ResponseEntity.ok(list);
        }


        // 카테고리 높은 2개 보내고 추천 도서들 받기
        private BookRecommendByCategoryDto getBooksByCategoryCount (Wishcategory categoryFirst,Wishcategory categorySecond) throws IOException {
//            List<Wishcategory> wishcategoryList = wishcategoryRepository.findByUser_UserId(userId);
//
//            // count 기준으로 오름차순 정렬되는 PriorityQueue 생성
//            PriorityQueue<Wishcategory> priorityQueue = new PriorityQueue<>(2, (w1, w2) -> Long.compare(w1.getCount(), w2.getCount()));
//
//            for (Wishcategory wishcategory : wishcategoryList) {
//                priorityQueue.offer(wishcategory);
//                if (priorityQueue.size() > 2) {
//                    priorityQueue.poll(); // 최소 값을 제거하여 상위 2개의 요소만 유지
//                }
//            }

            // ML 서버 URL 설정
            String urlStr = UriComponentsBuilder.fromHttpUrl(url)
                    .path("/api/recommend/wishlist/count")
                    .queryParam("preferred_categories",categoryFirst.getCategory())
                    .queryParam("preferred_categories",categorySecond.getCategory())
                    .toUriString();

            String response = restTemplate.getForObject(urlStr, String.class);
            log.info("category 기반 : {}", response);

            BookRecommendByCategoryDto dto = objectMapper.readValue(response, new TypeReference<BookRecommendByCategoryDto>() {});


            return dto;
        }

        // isbn 보내고 비슷한 도서들 받기
        public ResponseEntity<?> getSimilarBook (String bookId) throws IOException {

            // ML 서버 URL 설정
            String urlStr = UriComponentsBuilder.fromHttpUrl(url)
                    .path("/api/recommend/similar/" + bookId)
                    .toUriString();

            String response = restTemplate.getForObject(urlStr, String.class);
            log.info(response);
            return ResponseEntity.ok(parseJsonResponse(response));
        }


        // 받은 도서(isbn,coverUrl) 변환
        public List<BookByMlDto> parseJsonResponse (String response) throws IOException {
            List<BookByMlDto> bookList = new ArrayList<>();

            bookList = objectMapper.readValue(response, new TypeReference<List<BookByMlDto>>() {
            });
            return bookList;
        }
        // 즐겨찾기

        @Transactional
        public ResponseEntity<String> addWishlist (String isbn, Long userId) throws JsonProcessingException {
            // 유저를 찾고, 없으면 예외 발생
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 유저는 존재하지 않습니다."));
            System.out.println(user.getUserId());

            // 책을 찾고, 없으면 예외 발생
            Book optionalBook = bookRepository.findByBookId(isbn).orElseThrow(() -> new EntityNotFoundException("해당 책은 존재하지 않습니다."));
            System.out.println(optionalBook.getBookId());
//            Book book;

//            if (optionalBook != null) {
//                book = optionalBook;
//            } else {
//                book = new Book(findBookAladin(isbn));
//                bookRepository.save(book);
//            }
            // 위시리스트 생성
            Wishlist wishlist = new Wishlist(user, optionalBook);
            wishlistRepository.save(wishlist); // 위시리스트 저장

            // 상세조회시 위시 카테고리 up
            wishcategoryService.increaseWishcategory(userId, optionalBook.getCategory());

            return ResponseEntity.ok("위시리스트에 추가되었습니다.");
        }

        @Transactional
        public ResponseEntity<String> deleteWishlist (String isbn, Long userId) throws JsonProcessingException {
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
            wishcategoryService.decreaseWishcategory(userId, book.getCategory());
            return ResponseEntity.ok("위시리스트에 삭제되었습니다.");
        }


        private BookDetailDto findBookAladin (String isbn) throws JsonProcessingException {
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


        public ResponseEntity<?> getBookList (Long userId) throws IOException {

            List<Wishlist> wishlists = wishlistRepository.findByUserUserId(userId);
            ArrayList<String> bookList = new ArrayList<>();

            for (Wishlist wishlist : wishlists) {
                bookList.add(wishlist.getBook().getBookId());
            }


//        List<BookWishCategoryDto> bookWishCategoryDtos = new ArrayList<>();
//        List<Wishcategory> wishCateorys = wishcategoryRepository.findByUser_UserId(userId);
//        for (Wishcategory wishcategory : wishCateorys) {
//            BookWishCategoryDto bw = new BookWishCategoryDto(wishcategory);
//            bookWishCategoryDtos.add(bw);
//        }


            List<BookClickDto> bookClickDtos = new ArrayList<>();
            List<BookClick> bookClick = bookClickRepository.findByUserUserId(userId);

//            for (BookClick bookClick1 : bookClick) {
//                BookClickDto bt = new BookClickDto(bookClick1);
//                bookClickDtos.add(bt);
//            }

            Map<String, Long> clicksMap = new HashMap<>();
            for (BookClick bookClick1 : bookClick) {
                clicksMap.put(bookClick1.getBookId(),bookClick1.getClick());
            }

            // 유저 선호책 ,책별 클릭수 로 데이터 받아오기
//            BookRecommendDto br = new BookRecommendDto(bookList, bookClickDtos);

            BookRecommendDto br = new BookRecommendDto(bookList, clicksMap);


//            System.out.println("prefe : " + br.getUser_preferences().toString() + "click : " + br.getBookClickDtos().toString());

            log.info("prefe: {}, click: {}",br.getUser_preferences().toString(),br.getClicksMap().toString());

            // ML 서버 URL 설정
            String urlStr = UriComponentsBuilder.fromHttpUrl(url)
                    .path("/api/recommend/recommendations")
                    .toUriString();

            ResponseEntity<String> response = restTemplate.postForEntity(urlStr, br, String.class);

            String jsonResponse = response.getBody();

            List<BookByMlDto> booksByRecommend = objectMapper.readValue(jsonResponse, new TypeReference<List<BookByMlDto>>() {});


//            System.out.println("isbn:"+booksByRecommend.get(0).getIsbn13()+" cover}"+booksByRecommend.get(0).getCoverURL());




            // 카테고리 높은거 2개 보내기
            List<Wishcategory> wishcategoryList = wishcategoryRepository.findByUser_UserId(userId);

            // count 기준으로 오름차순 정렬되는 PriorityQueue 생성
            PriorityQueue<Wishcategory> priorityQueue = new PriorityQueue<>(2, (w1, w2) -> Long.compare(w1.getCount(), w2.getCount()));

            for (Wishcategory wishcategory : wishcategoryList) {
                priorityQueue.offer(wishcategory);
                if (priorityQueue.size() > 2) {
                    priorityQueue.poll(); // 최소 값을 제거하여 상위 2개의 요소만 유지
                }
            }
            Wishcategory categoryFirst =  priorityQueue.poll();
            Wishcategory categorySecond =  priorityQueue.poll();

            BookRecommendByCategoryDto booksByCategoryCount = getBooksByCategoryCount(categoryFirst,categorySecond);

            List<String> categories = new ArrayList<>();
            categories.add(categoryFirst.getCategory());
            categories.add(categorySecond.getCategory());
            BookRecommendResponse bookRecommendResponse = new BookRecommendResponse(categories,booksByRecommend,booksByCategoryCount.getFirst(),booksByCategoryCount.getSecond());

            return ResponseEntity.ok(bookRecommendResponse);
//        return ResponseEntity.ok(br);
        }
    }
