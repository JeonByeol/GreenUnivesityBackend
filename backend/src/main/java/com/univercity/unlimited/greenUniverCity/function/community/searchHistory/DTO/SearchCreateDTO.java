package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SearchCreateDTO {
    private Long userId;
    private String keyword;
    private String searchType;
    private LocalDateTime createdAt;

}
