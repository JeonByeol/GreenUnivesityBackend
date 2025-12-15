package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO;

import java.time.LocalDateTime;

public class SearchCreateDTO {
    private Long userId;
    private String keyword;
    private String searchType;
    private LocalDateTime createdAt;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
