package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.service;

import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.repository.PostRepository;
import com.univercity.unlimited.greenUniverCity.function.community.post.service.PostService;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity.SearchHistory;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.repository.SearchHistoryRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Builder
public class SearchServiceImpl implements SearchService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final ModelMapper mapper;
    private final PostRepository postRepository;
    private final PostService postService;


    // S-1) 전체 검색
    @Override
    public List<SearchResponseDTO> findAllSearch() {
        List<SearchResponseDTO> search=new ArrayList<>();
        for(SearchHistory i:searchHistoryRepository.findAll()){
            SearchResponseDTO r=mapper.map(i, SearchResponseDTO.class);
            search.add(r);
        }
        log.info("모든 Search를 조회하는 service 코드 실행");
        return search;
    }

    // S-7 ) 아이디 검색 기록 조회 (페이징)
    @Override
    public SearchResponseDTO findIdSearch(Long searchId) {
        SearchHistory search = searchHistoryRepository.findById(searchId)
                .orElseThrow(() -> new RuntimeException("게시물이 없습니다."));
        return mapper.map(search, SearchResponseDTO.class);
    }

    @Override
    public SearchResponseDTO createSearch(SearchCreateDTO dto) {
        // DTO -> 엔티티 (수동 매핑)
        SearchHistory entity = SearchHistory.builder()
                .userId(dto.getUserId())
//                .searchType(dto.getSearchType())
                .keyword(dto.getKeyword())
                .build();
        return null;
    }

    @Override
    public SearchResponseDTO updateSearch(SearchUpdateDTO dto) {
        return null;
    }

    @Override
    public void deleteSearch(Long searchId) {
        searchHistoryRepository.deleteById(searchId);
    }


    @Override
    public ResponseEntity<PostResponseDTO> updatePost(
            @RequestBody PostUpdateDTO dto
    ) {
        PostResponseDTO result = postService.postUpdate(dto.getPostId(), dto);
        return ResponseEntity.ok(result);
    }
    }

//        // 2) 엔티티 조회
//        SearchHistory entity = searchHistoryRepository.findById(searchId)
//                .orElseThrow(() -> new RuntimeException("게시물이 없습니다."));
//
//        // 3) 값 변경
//        entity.setUserId(entity.getUserId());
//
//        // 4) 저장
//        SearchHistory saved = searchHistoryRepository.save(entity);
//
//        // 5) 응답 DTO
//        return SearchResponseDTO.builder()
//                .userId(saved.getUserId())
//                .keyword(saved.getKeyword())
//                .build();
//    }



//    // S-8) 아이디 검색 기록 삭제
//    @Override
//    public void deleteSearch(Long searchId) {
//        searchHistoryRepository.deleteById(searchId);
//    }

//    public ResponseEntity<SearchResponseDTO> UpdateSearch(@RequestBody SearchResponseDTO dto) {
//        // dto.boardId 안에 수정할 boardId가 들어 있어야 함
//        SearchResponseDTO updated = searchService.updateSearch(dto);
//        return ResponseEntity.ok(updated);

//    // S-10) 아이디 검색 기록 생성
//    @Override
//    public SearchResponseDTO createSearch(SearchCreateDTO dto) {
//        SearchHistory entity = mapper.map(dto, SearchHistory.class);
//        entity.setCreatedAt(LocalDateTime.now());
//
//        SearchHistory saved = searchHistoryRepository.save(entity);
//
//        SearchResponseDTO response = mapper.map(saved, SearchResponseDTO.class);
//        return response;
//    }

//    @Override
//    public SearchResponseDTO getMySearchLogs(Long userId, Pageable pageable) {
//        SearchResponseDTO r =mapper.map(searchHistoryRepository.findById(userId), SearchResponseDTO.class);
//        return r;
//    }
//    // 검색 로그 저장 (중요)
//    private void saveSearchLog(Long userId, String keyword, SearchType type, Integer resultCount) {
//
//        SearchHistory history = SearchHistory.builder()
//                .userId(userId)
//                .keyword(keyword)
//                .searchType(type)     // POST or ALL
//                .build();
//
//        searchHistoryRepository.save(history);
//    }

//}
