package com.univercity.unlimited.greenUniverCity.function.community.post.dto;

import lombok.*;

import java.time.LocalDateTime;

// P-1) DTO 작성
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PostResponseDTO {
    private String boardName;
    private Long userId;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
