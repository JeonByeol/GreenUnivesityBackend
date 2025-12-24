package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.controller;

import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 검색 API Controller
 * base url : /api/search
 */
@RestController
@RequestMapping("/api/search")
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    //검색
    @GetMapping("/all")
    public ResponseEntity<List<SearchResponseDTO>> searchAll(Pageable pageable) {
        log.info("전체 검색 요청");
        List<SearchResponseDTO> result = searchService.findAllSearch();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/board")
    public ResponseEntity<String> searchInBoard() {
        return ResponseEntity.ok("게시판 검색은 아직 구현 전입니다.");
    }


    @GetMapping("/post")
    public ResponseEntity<String> searchInPost() {
        return ResponseEntity.ok("게시물 검색은 아직 구현 전입니다.");
    }

    //추가
    @PostMapping("/create")
    public ResponseEntity<SearchResponseDTO> createSearch(
            @RequestBody SearchCreateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail)
    {
        return ResponseEntity.ok(searchService.createSearch(dto));
        // 또는 return ResponseEntity.ok(created);
    }

    //수정
    @PutMapping("/update")
    public ResponseEntity<SearchResponseDTO> updatePost(
            @RequestBody SearchUpdateDTO dto
    ) {
        SearchResponseDTO result = searchService.updateSearch(dto);
        return ResponseEntity.ok(result);
    }
    //삭제
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteMyLogs(@PathVariable Long userId) {
        log.info("내 검색 기록 삭제 userId={}", userId);
        searchService.deleteSearch(userId);
        return ResponseEntity.noContent().build();
    }
}
