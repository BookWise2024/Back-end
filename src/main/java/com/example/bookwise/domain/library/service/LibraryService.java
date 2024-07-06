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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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


    /// 수정 필요 ///
    @Transactional
    public String createLibraryInitDB() throws Exception {


        String urlStr = "http://data4library.kr/api/libSrch?authKey=" + libraryInitDB.getLibraryBigdataKey()
                + "&pageNo=" + libraryInitDB.getPageNo()
                + "&pageSize=" + libraryInitDB.getPageSize()
                + "&region=" + libraryInitDB.getRegion()
                + "&format=" + libraryInitDB.getFormat();

        String response = restTemplate.getForObject(urlStr, String.class);

        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode responseNode = rootNode.get("response");
        JsonNode libsNode = responseNode.get("libs");

        List<LibraryComparisonDto> libraryList = new ArrayList<>();

        for (JsonNode libNode : libsNode) {
            JsonNode info = libNode.get("lib");

            LibraryComparisonDto libraryComparisonDto = new LibraryComparisonDto(
                    info.get("libCode").asLong(),
                    info.get("libName").asText().replace(" ", ""),
                    info.get("address").asText().replace(" ", ""),
                    info.get("homepage").asText().replace(" ", ""),
                    info.get("tel").asText().replace(" ", "")
            );

            libraryList.add(libraryComparisonDto);

        }

        String urlSt = "http://openapi.seoul.go.kr:8088/" + libraryInitDB.getSeoulLibraryKey()
                + "/" + libraryInitDB.getFormat()
                + "/SeoulPublicLibraryInfo/" + libraryInitDB.getPageNo()
                + "/" + libraryInitDB.getPageSize() + "/";


        String res = restTemplate.getForObject(urlSt, String.class);

        JsonNode root = objectMapper.readTree(res);
        JsonNode SeoulPublicLibraryInfo = root.get("SeoulPublicLibraryInfo");
        JsonNode rows = SeoulPublicLibraryInfo.get("row");

        for (JsonNode info : rows) {
            String name = info.get("LBRRY_NAME").asText();
            String address = info.get("ADRES").asText();
            String url = info.get("HMPG_URL").asText();
            String opTime = info.get("OP_TIME").asText();
            String closeTime = info.get("FDRM_CLOSE_DATE").asText();
            Double latitude = info.get("XCNTS").asDouble();
            Double longitude = info.get("YDNTS").asDouble();
            String tel = info.get("TEL_NO").asText();


            for (int i = 0; i < libraryList.size(); i++) {
                LibraryComparisonDto dto = libraryList.get(i);
                if (dto.getName().equals(name.replace(" ", "")) || dto.getAddress().equals(address.replace(" ", "")) || dto.getUrl().equals(url.replace(" ", "")) || dto.getTel().equals(tel.replace(" ", ""))) {
                    Library library = new Library(dto.getLibraryId(), name, address, url, opTime, closeTime, latitude, longitude);
                    libraryRepository.save(library);
                    libraryList.remove(i);
                }

            }

        }

        return "Success";
    }


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