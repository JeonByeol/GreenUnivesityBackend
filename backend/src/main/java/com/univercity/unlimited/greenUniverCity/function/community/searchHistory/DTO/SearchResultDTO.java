package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SearchResultDTO {
    private Long userId;
    private String keyword;    // 어떤 검색어로 검색했는지
    private Integer resultCount; // 검색 결과 개수 (지금은 더미)

    // 빈 검색어일 때 쓰는 편의 메서드
    public static SearchResultDTO empty() {
        return SearchResultDTO.builder()
                .keyword("")
                .resultCount(0)
                .build();
    }
}
