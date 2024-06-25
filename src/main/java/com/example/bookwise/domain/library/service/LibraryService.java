package com.example.bookwise.domain.library.service;


import com.example.bookwise.domain.library.entity.Library;
import com.example.bookwise.domain.library.repository.LibraryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${librarybigdata.api.key}")
    private String authKey; //인증키
    private String pageNo = "1"; //페이지 번호
    private String pageSize = "319"; //페이지 사이즈
    private String region = "11"; // 서울지역번호
    private String format = "json"; // 포맷 형식

    /// 수정 필요 ///
    @Transactional
    public String createLibraryInitDB() throws Exception {


        //    // 서울내 공공 도서관 : 206개 작은 도서관 : 113개
        String urlStr = "http://data4library.kr/api/libSrch?authKey=" + authKey + "&pageNo=" + pageNo + "&pageSize=" + pageSize + "&region=" + region + "&format=" + format;

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

}
