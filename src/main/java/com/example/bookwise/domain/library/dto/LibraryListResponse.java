package com.example.bookwise.domain.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class LibraryListResponse {
    List<LibraryDistanceDto> libraryList;
}
