package com.example.bookwise.domain.initdb.service;


import com.example.bookwise.domain.book.entity.Book;
import com.example.bookwise.domain.book.repository.BookRepository;
import com.example.bookwise.domain.library.dto.LibraryComparisonDto;
import com.example.bookwise.domain.library.dto.LibraryInitDBDto;
import com.example.bookwise.domain.library.entity.Library;
import com.example.bookwise.domain.library.repository.LibraryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InitDbService {


    private final LibraryRepository libraryRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final LibraryInitDBDto libraryInitDB;
    private final BookRepository bookRepository;

    static int count = 0;
    static int cnt = 0;


    @Transactional
    public String createBookInitDB() throws Exception {
        String excelFilePath = "src/main/resources/bookSampleData.xlsx";
        InputStream is = new FileInputStream(excelFilePath);

        if (is == null) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: bookSampleData.xls");
        }
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        List<Book> bookList = new ArrayList<>();
        for (Row row : sheet) {


            Book book = new Book(getCellValue(row,4), getCellValue(row,6), getCellValue(row,0),getCellValue(row,1), getCellValue(row,9), getCellValue(row,2),getCellValue(row,8), getCellValue(row,6), getCellValue(row,7), getCellValue(row,3)
            );
            bookRepository.save(book);
        }


        // count(*) : 32852
       // 공백: 6744 있음: 26108
        return "공백: "+count+ " 있음: "+cnt;
    }

    private String getCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) {
            if(cellNum == 3)count++;
            return "";
        }
        if(cellNum == 3)cnt++;

        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue());

    }


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


}
