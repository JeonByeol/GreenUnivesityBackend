package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.service;

import com.univercity.unlimited.greenUniverCity.function.community.post.repository.PostRepository;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchResultDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity.SearchHistory;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity.SearchType;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.repository.SearchHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class SearchServiceImpl implements SearchService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final ModelMapper mapper;
    private final PostRepository postRepository;


    // S-1) 전체 검색
    @Override
    public List<SearchResponseDTO> searchAll(SearchCreateDTO dto, Pageable pageable) {

        // S-2) 키워드 전처리 (공백 제거)

        // S-3) 검색 실행 (지금은 더미 데이터로 반환)

        // S-5) 검색 로그 저장

        // S-6) 검색 결과 반환 (공통 DTO)
        List<SearchResponseDTO> search=new ArrayList<>();
        for(SearchHistory i:searchHistoryRepository.searchAll()){
            SearchResponseDTO r=mapper.map(i, SearchResponseDTO.class);
            search.add(r);
        }
        log.info("모든 Search를 조회하는 service 코드 실행");
        return search;
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

    @Override
    public List<SearchResponseDTO> searchAll(Pageable pageable) {
        return List.of();
    }

}
