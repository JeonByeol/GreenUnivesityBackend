package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SearchUpdateDTO {
    private Long userId;
    private String keyword;
    private String searchType;
    private LocalDateTime createdAt;
}
