package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.service;

import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchResultDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity.SearchHistory;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity.SearchType;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.repository.SearchHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class SearchServiceImpl implements SearchService {

    private final SearchHistoryRepository searchHistoryRepository;


    // S-1) 전체 검색
    @Override
    public SearchResultDTO searchAll(SearchCreateDTO dto, Pageable pageable) {

        // S-2) 키워드 전처리 (공백 제거)
        String keyword = dto.getKeyword().trim();
        if (keyword.isEmpty()) {
            // 빈 검색어 처리
            return SearchResultDTO.empty();
        }

        // S-3) 검색 실행 (지금은 더미 데이터로 반환)
        // 나중에 게시판/게시글/커뮤니티 검색을 여기에 붙이면 됨
        int fakeResultCount = 10;

        // S-5) 검색 로그 저장
        saveSearchLog(dto.getUserId(), keyword, SearchType.ALL, fakeResultCount);

        // S-6) 검색 결과 반환 (공통 DTO)
        return SearchResultDTO.builder()
                .keyword(keyword)
                .resultCount(fakeResultCount)
                .build();
    }


    // 검색 로그 저장 (중요)
    private void saveSearchLog(Long userId, String keyword, SearchType type, Integer resultCount) {

        SearchHistory history = SearchHistory.builder()
                .userId(userId)
                .keyword(keyword)
                .searchType(type)     // POST or ALL
                .build();

        searchHistoryRepository.save(history);
    }


    // S-7 ) 아이디 검색 기록 조회 (페이징)
    @Override
    public Page<SearchHistory> getMySearchLogs(Long userId, Pageable pageable) {
        return searchHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }


    // S-8) 아이디 검색 기록 삭제
    @Override
    @Transactional
    public void deleteAllMyLogs(Long userId) {
        searchHistoryRepository.deleteByUserId(userId);
    }


}
