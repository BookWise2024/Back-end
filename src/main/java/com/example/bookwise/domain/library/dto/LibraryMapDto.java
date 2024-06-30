package com.example.bookwise.domain.library.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LibraryMapDto {
    Double latitude;
    Double longitude;
    double range = 3L;
    String sort = "distance";


}

