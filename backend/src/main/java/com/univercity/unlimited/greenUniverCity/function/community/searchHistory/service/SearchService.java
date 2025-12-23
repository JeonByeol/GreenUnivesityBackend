package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.service;

import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchResultDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchService {

    //전체 검색
    List<SearchResponseDTO> searchAll(SearchCreateDTO dto, Pageable pageable);

    // 내 검색 기록 조회 (페이징)
    Page<SearchHistory> getMySearchLogs(Long userId, Pageable pageable);

    // 내 검색 기록 전체 삭제
    void deleteAllMyLogs(Long userId);

    List<SearchResponseDTO> searchAll(Pageable pageable);
}
