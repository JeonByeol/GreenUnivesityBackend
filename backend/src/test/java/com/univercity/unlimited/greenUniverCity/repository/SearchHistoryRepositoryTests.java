package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity.SearchHistory;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity.SearchType;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.repository.SearchHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class SearchHistoryRepositoryTests {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    /**
     * 더미 검색 기록 5개 저장 테스트
     */
    @Test
    @Tag("push")
    public void createDummySearchHistory() {

        // 더미 5개 생성
        for (int i = 1; i <= 5; i++) {
            SearchHistory history = SearchHistory.builder()
                    .userId(1L)                         // userId는 임의로 1로 고정
                    .keyword("테스트검색어" + i)         // 검색어
                    .searchType(SearchType.ALL)        // 전체 검색 타입
                    .build();

            SearchHistory saved = searchHistoryRepository.save(history);

            // 저장이 됐는지 검증
            assertThat(saved.getId()).isNotNull();
            log.info("저장된 검색 기록 {}: {}", i, saved);
        }
    }

    /**
     * 전체 검색 기록 불러오기 테스트
     */
    @Test
    @Tag("push")
    public void findAllSearchHistory() {

        List<SearchHistory> list = searchHistoryRepository.findAll();

        assertThat(list).isNotEmpty();
        log.info("전체 검색 기록 개수: {}", list.size());

        list.forEach(h -> log.info("검색기록 -> {}", h));
    }
}
