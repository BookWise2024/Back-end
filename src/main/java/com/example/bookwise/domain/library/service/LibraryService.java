package com.example.bookwise.domain.library.service;


import com.example.bookwise.domain.library.dto.*;
import com.example.bookwise.domain.library.entity.Library;
import com.example.bookwise.domain.library.repository.LibraryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
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


    /// 수정 필요 ///
    @Transactional
    public String createLibraryInitDB() throws Exception {
        // 서울내 공공 도서관 : 206개 작은 도서관 : 113개
        String urlStr = "http://data4library.kr/api/libSrch?authKey=" + libraryInitDB.getAuthKey()
                + "&pageNo=" + libraryInitDB.getPageNo()
                + "&pageSize=" + libraryInitDB.getPageSize()
                + "&region=" + libraryInitDB.getRegion()
                + "&format=" + libraryInitDB.getFormat();

        String response = restTemplate.getForObject(urlStr, String.class);

        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode responseNode = rootNode.get("response");
        JsonNode libsNode = responseNode.get("libs");

        for (JsonNode libNode : libsNode) {
            JsonNode info = libNode.get("lib");

            Library library = new Library(
                    info.get("libCode").asLong(),
                    info.get("libName").asText(),
                    info.get("address").asText(),
                    info.get("homepage").asText(),
                    info.get("operatingTime").asText(),
                    info.get("closed").asText(),
                    info.get("latitude").asDouble(),
                    info.get("longitude").asDouble()
            );
            libraryRepository.save(library);
        }
        return "SUCCESS";
    }


    public LibraryListResponse getLibraryByDistance(LibraryMapDto libraryMapDto) {

        // 범위안에 있는 도서관들 조회
        List<Library> libraries = libraryRepository.findLibrariesWithinDistance(libraryMapDto.getLatitude(), libraryMapDto.getLongitude(), libraryMapDto.getRange());

        List<LibraryDistanceDto> libraryDtoList = new ArrayList<>();
        for (Library l : libraries) {
            double dis = Library.getDistance(libraryMapDto.getLatitude(), libraryMapDto.getLongitude(), l.getLatitude(), l.getLongitude());

            libraryDtoList.add(LibraryDistanceDto.builder().libraryId(l.getLibraryId()).name(l.getName()).address(l.getAddress()).distance(Math.round(dis * 10) / 10.0).build());
        }

        // 가까운순으로 정렬
        List<LibraryDistanceDto> result = libraryDtoList.stream().sorted((o1, o2) -> Double.compare(o1.getDistance(), o2.getDistance()))
                .collect(Collectors.toList());

        return new LibraryListResponse(result);
    }


    public LibraryDetailResponse getLibraryDetail(long libraryId) throws Exception {

        Library library = libraryRepository.findByLibraryId(libraryId)
                .orElseThrow(() -> new Exception("잘못된 정보입니다."));


        LibraryDetailResponse libraryDetailResponse = LibraryDetailResponse.builder()
                .name(library.getName())
                .address(library.getAddress())
                .url(library.getUrl())
                .opTime(library.getOpTime())
                .closeTime(library.getCloseTime())
                .build();


        return libraryDetailResponse;
    }

}