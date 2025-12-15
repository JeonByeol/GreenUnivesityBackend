package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.controller;

import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchResultDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity.SearchHistory;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// /api/search 로 시작하는 모든 요청 처리
@RestController
@RequestMapping("/api/search")
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    // final + @RequiredArgsConstructor → 자동 DI
    private final SearchService searchService;

    // S-1) 전체 검색 (게시판, 게시물, 커뮤니티 등 통합 검색)
    // 예: GET /api/search/all?userId=1&keyword=자바&page=0&size=10
    @GetMapping("/all")
    public ResponseEntity<SearchResultDTO> searchAll(
            Pageable pageable
    ) {
        log.info("전체 검색 요청 userId={}, keyword={}");

        // DTO 만들어서 서비스로 넘기기
        SearchCreateDTO dto = new SearchCreateDTO();

        SearchResultDTO result = searchService.searchAll(dto, pageable);
        return ResponseEntity.ok(result);
    }

    // S-2) 키워드 검색 (예: 게시물만 검색 등으로 확장 가능)
    // 지금은 전체 검색과 동일하게 동작하게 만들어두고,
    // 나중에 "게시물만 검색" 같은 로직을 따로 분리할 수 있음.
    // 예: GET /api/search/keyword?userId=1&keyword=자바&page=0&size=10
    @GetMapping("/keyword")
    public ResponseEntity<SearchResultDTO> searchByKeyword(
            @RequestParam(required = false) Long userId,
            @RequestParam String keyword,
            Pageable pageable
    ) {
        log.info("키워드 검색 요청 userId={}, keyword={}", userId, keyword);

        SearchCreateDTO dto = new SearchCreateDTO();
        dto.setUserId(userId != null ? userId : 0L); // userId 없으면 0L 같은 기본값
        dto.setKeyword(keyword);

        SearchResultDTO result = searchService.searchAll(dto, pageable);
        return ResponseEntity.ok(result);
    }

    // S-3) 검색 로그만 따로 저장하고 싶을 때(선택)
    // 예: POST /api/search/log
    @PostMapping("/log")
    public ResponseEntity<Void> saveSearchLog(@RequestBody SearchCreateDTO dto) {
        log.info("검색 로그 단독 저장 userId={}, keyword={}", dto.getUserId(), dto.getKeyword());

        // 검색 없이 로그만 남기고 싶다면,
        // 별도의 서비스 메서드를 만들거나 searchAll에서 Pageable.unpaged()로 호출해서 처리 가능
        searchService.searchAll(dto, Pageable.unpaged());  // 지금 구조에서는 이렇게 재사용 가능

        return ResponseEntity.ok().build();
    }

    // S-4) 게시판에서 검색 (TODO: 게시판 전용 검색 로직 만들면 여기서 호출)
    @GetMapping("/board")
    public ResponseEntity<String> searchInBoard() {
        // 나중에 Board 전용 검색 로직 추가
        return ResponseEntity.ok("게시판 검색은 아직 구현 전입니다.");
    }

    // S-5) 게시물에서 검색 (TODO: Post 전용 검색 로직 만들면 여기서 호출)
    @GetMapping("/post")
    public ResponseEntity<String> searchInPost() {
        // 나중에 Post 전용 검색 로직 추가
        return ResponseEntity.ok("게시물 검색은 아직 구현 전입니다.");
    }

    // S-8) 내 검색 기록 조회
    // 예: GET /api/search/logs?userId=1&page=0&size=10
    @GetMapping("/logs")
    public ResponseEntity<Page<SearchHistory>> getMyLogs(
            @RequestParam Long userId,
            Pageable pageable
    ) {
        log.info("내 검색 기록 조회 userId={}", userId);
        Page<SearchHistory> logs = searchService.getMySearchLogs(userId, pageable);
        return ResponseEntity.ok(logs);
    }

    // S-9) 내 검색 기록 삭제
    // 예: DELETE /api/search/logs/1
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteMyLogs(@PathVariable Long userId) {
        log.info("내 검색 기록 삭제 userId={}", userId);
        searchService.deleteAllMyLogs(userId);
        return ResponseEntity.noContent().build();
    }
}
