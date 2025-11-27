package com.univercity.unlimited.greenUniverCity.function.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
// P-1) DTO 작성
public class PostUpdateDTO {
    private String title;
    private String content;
    private Long authorId;
    private Long boardId;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
