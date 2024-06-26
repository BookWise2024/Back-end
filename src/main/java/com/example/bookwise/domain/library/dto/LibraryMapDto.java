package com.example.bookwise.domain.library.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class LibraryMapDto {
    Double latitude;
    Double longitude;
    double range = 5L;
}

