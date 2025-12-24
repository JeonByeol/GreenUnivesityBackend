package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class SearchResponseDTO {
    private Long userId;
    private String keyword;
    private String searchType;
    private LocalDateTime createAt;
}
