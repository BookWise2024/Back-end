package com.example.bookwise.domain.library.service;


import com.example.bookwise.domain.book.dto.BookByMlDto;
import com.example.bookwise.domain.book.dto.BookDetailDto;
import com.example.bookwise.domain.book.entity.Book;
import com.example.bookwise.domain.book.repository.BookRepository;
import com.example.bookwise.domain.library.dto.*;
import com.example.bookwise.domain.library.entity.Library;
import com.example.bookwise.domain.library.repository.LibraryRepository;
import com.example.bookwise.domain.wishcategory.entity.Wishcategory;
import com.example.bookwise.domain.wishcategory.repository.WishcategoryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final LibraryInitDBDto libraryInitDB;
    private final BookRepository bookRepository;

    // 위치 기반 도서관 조회
    public LibraryListResponse getLibraryByDistance(LibraryMapDto libraryMapDto) {

        // 범위안에 있는 도서관들 조회
        List<Library> libraries = libraryRepository.findLibrariesWithinDistance(libraryMapDto.getLatitude(), libraryMapDto.getLongitude(), libraryMapDto.getRange());

        List<LibraryDistanceDto> libraryDtoList = new ArrayList<>();
        for (Library l : libraries) {
            double dis = Library.getDistance(libraryMapDto.getLatitude(), libraryMapDto.getLongitude(), l.getLatitude(), l.getLongitude());

            libraryDtoList.add(LibraryDistanceDto.builder()
                    .libraryId(l.getLibraryId())
                    .name(l.getName())
                    .address(l.getAddress())
                    .distance(Math.round(dis * 10) / 10.0)
                    .latitude(l.getLatitude())
                    .longitude(l.getLongitude()).build());
        }

        // 가까운순으로 정렬
        List<LibraryDistanceDto> result = libraryDtoList.stream().sorted((o1, o2) -> Double.compare(o1.getDistance(), o2.getDistance()))
                .collect(Collectors.toList());


        return new LibraryListResponse(result);
    }


    // 도서관 상세정보 조회
    public LibraryDetailResponse getLibraryDetail(long libraryId) throws Exception {

        Library library = libraryRepository.findByLibraryId(libraryId)
                .orElseThrow(() -> new Exception("잘못된 정보입니다."));


        return LibraryDetailResponse.builder()
                .name(library.getName())
                .address(library.getAddress())
                .url(library.getUrl())
                .opTime(library.getOpTime())
                .closeTime(library.getCloseTime())
                .latitude(library.getLatitude())
                .longitude(library.getLongitude())
                .build();
    }


    // 위치기반 도서관 조회(도서정보)
    public LibraryListByBookResponse getLibraryByBook(String bookId, LibraryMapDto libraryMapDto) throws Exception {

        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기


        // 범위안에 있는 도서관들 조회
        List<Library> libraries = libraryRepository.findLibrariesWithinDistance(libraryMapDto.getLatitude(), libraryMapDto.getLongitude(), libraryMapDto.getRange());

        PriorityQueue<LibraryDistanceByBookDto> yBookQueue = new PriorityQueue<>(Comparator.comparingDouble(LibraryDistanceByBookDto::getDistance));
        PriorityQueue<LibraryDistanceByBookDto> nBookQueue = new PriorityQueue<>(Comparator.comparingDouble(LibraryDistanceByBookDto::getDistance));

        // 거리계산 및 도서들의 보유/미보유 추가
        for (Library l : libraries) {
            double dis = Library.getDistance(libraryMapDto.getLatitude(), libraryMapDto.getLongitude(), l.getLatitude(), l.getLongitude());

            // 보유한지 안한지 api 불러오기
            HasBookDto hasBook = getHasBook(bookId, l.getLibraryId());
            LibraryDistanceByBookDto dto = LibraryDistanceByBookDto.builder()
                    .libraryId(l.getLibraryId())
                    .name(l.getName())
                    .address(l.getAddress())
                    .distance(Math.round(dis * 10) / 10.0)
                    .latitude(l.getLatitude())
                    .longitude(l.getLongitude())
                    .hasBook(hasBook.getHasBook())
                    .loanAvailable(hasBook.getLoanAvailable())
                    .build();
            if (hasBook.equals("Y")) {
                yBookQueue.add(dto);
            } else {
                nBookQueue.add(dto);
            }
        }

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime) / 1000; //두 시간에 차 계산
        System.out.println("시간차이(m) : " + secDiffTime);

        // 거리,보유순에 따라 정렬시키기
        List<LibraryDistanceByBookDto> result = new ArrayList<>();
        if (libraryMapDto.getSort().equals("possession")) {
            while (!yBookQueue.isEmpty()) {
                result.add(yBookQueue.poll());
            }
            while (!nBookQueue.isEmpty()) {
                result.add(nBookQueue.poll());
            }
        } else if (libraryMapDto.getSort().equals("distance")) {
            PriorityQueue<LibraryDistanceByBookDto> allBookQueue = new PriorityQueue<>(Comparator.comparingDouble(LibraryDistanceByBookDto::getDistance));
            allBookQueue.addAll(yBookQueue);
            allBookQueue.addAll(nBookQueue);
            while (!allBookQueue.isEmpty()) {
                result.add(allBookQueue.poll());
            }
        }

        return new LibraryListByBookResponse(result);
    }

    // 위치기반 도서관 조회(도서정보)
    public LibraryListByBookResponse getLibraryByBookTest(String bookId, LibraryMapDto libraryMapDto) throws Exception {

        // 범위안에 있는 도서관들 조회
        List<Library> libraries = libraryRepository.findLibrariesWithinDistance(libraryMapDto.getLatitude(), libraryMapDto.getLongitude(), libraryMapDto.getRange());

//        PriorityQueue<LibraryDistanceByBookDto> yBookQueue = new PriorityQueue<>(Comparator.comparingDouble(LibraryDistanceByBookDto::getDistance));
//        PriorityQueue<LibraryDistanceByBookDto> nBookQueue = new PriorityQueue<>(Comparator.comparingDouble(LibraryDistanceByBookDto::getDistance));

        ConcurrentLinkedQueue<LibraryDistanceByBookDto> yBookQueue = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<LibraryDistanceByBookDto> nBookQueue = new ConcurrentLinkedQueue<>();

        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기

        // 비동기 처리를 위한 ExecutorService 생성
        var executor = Executors.newCachedThreadPool(); // 적절한 스레드 풀 크기로 설정 // 동적으로 쓰레드 생성

        // 각 도서관에 대해 비동기 호출 수행
        List<CompletableFuture<Void>> futures = libraries.stream()
                .map(library -> CompletableFuture.runAsync(() -> {
                    try {
                        double dis = Library.getDistance(libraryMapDto.getLatitude(), libraryMapDto.getLongitude(), library.getLatitude(), library.getLongitude());
                        // 보유한지 안한지 api 불러오기
                        HasBookDto hasBook = getHasBook(bookId, library.getLibraryId());
                        LibraryDistanceByBookDto dto = LibraryDistanceByBookDto.builder()
                                .libraryId(library.getLibraryId())
                                .name(library.getName())
                                .address(library.getAddress())
                                .distance(Math.round(dis * 10) / 10.0)
                                .latitude(library.getLatitude())
                                .longitude(library.getLongitude())
                                .hasBook(hasBook.getHasBook())
                                .loanAvailable(hasBook.getLoanAvailable())
                                .build();
                        if (hasBook.equals("Y")) {
                            synchronized (yBookQueue) {
                                yBookQueue.add(dto);
                            }
                        } else {
                            synchronized (nBookQueue) {
                                nBookQueue.add(dto);
                            }
                        }
                    } catch (Exception e) {     // 예외처리 조정하기
                        e.printStackTrace();
                    }
                }, executor))
                .collect(Collectors.toList());

        // 모든 비동기 작업 완료 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime) / 1000; //두 시간에 차 계산
        System.out.println("시간차이(m) : " + secDiffTime);

        // 거리,보유순에 따라 정렬시키기
        List<LibraryDistanceByBookDto> result = new ArrayList<>();
        if (libraryMapDto.getSort().equals("possession")) {
            while (!yBookQueue.isEmpty()) {
                result.add(yBookQueue.poll());
            }
            while (!nBookQueue.isEmpty()) {
                result.add(nBookQueue.poll());
            }
        } else if (libraryMapDto.getSort().equals("distance")) {
            PriorityQueue<LibraryDistanceByBookDto> allBookQueue = new PriorityQueue<>(Comparator.comparingDouble(LibraryDistanceByBookDto::getDistance));
            allBookQueue.addAll(yBookQueue);
            allBookQueue.addAll(nBookQueue);
            while (!allBookQueue.isEmpty()) {
                result.add(allBookQueue.poll());
            }
        }

        // Executor 서비스 종료
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        return new LibraryListByBookResponse(result);
    }


    // 도서관 상세정보 조회(도서기반)
    public LibraryDetailByBookResponse getLibraryDetailByBook(String bookId, long libraryId, HasBookDto hasBookDto) throws Exception {

        Library library = libraryRepository.findByLibraryId(libraryId)
                .orElseThrow(() -> new Exception("잘못된 정보입니다."));

        Book book = bookRepository.findByBookId(bookId).orElseThrow();

        HasBookDetailDto dto = HasBookDetailDto.builder().bookId(book.getBookId())
                .coverUrl(book.getCoverUrl())
                .title(book.getTitle())
                .author(book.getAuthor())
                .styleDesc(book.getStyleDesc())
                .publishDate(book.getPublishDate())
                .publisher(book.getPublisher())
                .category(book.getCategory())
                .subcategory(book.getSubcategory())
                .description(book.getDescription())
                .hasBook(hasBookDto.getHasBook())
                .loanAvailable(hasBookDto.getLoanAvailable())
                .build();


        LibraryDetailResponse libraryDetailResponse = LibraryDetailResponse.builder()
                .name(library.getName())
                .address(library.getAddress())
                .url(library.getUrl())
                .opTime(library.getOpTime())
                .closeTime(library.getCloseTime())
                .latitude(library.getLatitude())
                .longitude(library.getLongitude())
                .build();


        LibraryDetailByBookResponse libraryDetailByBookResponse = LibraryDetailByBookResponse.builder()
                .library(libraryDetailResponse)
                .book(dto)
                //      .similarBooks()     비슷한 책 리스트 만들면 추가
                .build();


        return libraryDetailByBookResponse;
    }


    // 보유,대출 가능한지 확인코드
    public HasBookDto getHasBook(String bookId, Long libraryId) throws Exception {

        String urlStr = "http://data4library.kr/api/bookExist?authKey=" + libraryInitDB.getLibraryBigdataKey()
                + "&libCode=" + libraryId
                + "&isbn13=" + bookId
                + "&format=" + libraryInitDB.getFormat();

        String response = restTemplate.getForObject(urlStr, String.class);
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode responseNode = rootNode.get("response");
        JsonNode info = responseNode.get("result");
        return new HasBookDto(info.get("hasBook").asText(), info.get("loanAvailable").asText());
    }

}