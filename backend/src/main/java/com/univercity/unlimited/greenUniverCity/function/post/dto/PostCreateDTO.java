package com.univercity.unlimited.greenUniverCity.function.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

// P-1) DTO 작성
@Getter
@Builder
@AllArgsConstructor
public class PostCreateDTO {
    private String boardName;
    private Long userid;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
