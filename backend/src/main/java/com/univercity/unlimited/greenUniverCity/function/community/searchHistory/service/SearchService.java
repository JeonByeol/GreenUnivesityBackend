package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.service;

import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchResultDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.DTO.SearchUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface SearchService {
    // search 생성
    List<SearchResponseDTO> findAllSearch();

    // 단건 조회
    SearchResponseDTO findIdSearch(Long searchId);

    // 생성
    SearchResponseDTO createSearch(SearchCreateDTO dto);

    // 추가
    SearchResponseDTO updateSearch(SearchUpdateDTO dto);

    // 삭제
    void deleteSearch(Long searchId);

    @PutMapping("/update")
    ResponseEntity<PostResponseDTO> updatePost(
            @RequestBody PostUpdateDTO dto
    );
}
