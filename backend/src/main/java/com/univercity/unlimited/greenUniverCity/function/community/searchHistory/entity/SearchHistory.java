package com.univercity.unlimited.greenUniverCity.function.community.searchHistory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_SearchHistory")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Long id;   // PK

    // 누가 검색했는가
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 검색어
    @Column(name = "keyword", nullable = false, length = 255)
    private String keyword;

    // Post 내 검색 ? 전체 검색?
    @Enumerated(EnumType.STRING)
    @Column(name = "search_type", nullable = false, length = 20)
    private SearchType searchType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}

