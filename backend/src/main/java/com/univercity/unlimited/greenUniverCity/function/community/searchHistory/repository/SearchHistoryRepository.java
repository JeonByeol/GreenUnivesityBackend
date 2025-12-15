package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.repository;

import com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory,Long> {
    SearchHistory save(SearchHistory history);

    void deleteByUserId(Long userId);

    Page<SearchHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<SearchHistory> findAll();
}
