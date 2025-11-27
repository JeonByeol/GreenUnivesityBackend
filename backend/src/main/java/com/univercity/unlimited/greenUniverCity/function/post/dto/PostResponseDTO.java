package com.univercity.unlimited.greenUniverCity.function.post.dto;

import com.univercity.unlimited.greenUniverCity.function.post.entity.Post;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.stream.DoubleStream;
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
