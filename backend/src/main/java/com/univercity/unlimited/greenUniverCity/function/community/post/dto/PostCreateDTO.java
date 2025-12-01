package com.univercity.unlimited.greenUniverCity.function.community.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

// P-1) DTO 작성
@Getter
@Builder
@AllArgsConstructor
public class PostCreateDTO {
    private Long boardId; // 게시판
    private Long userId; //유저 아이디
    private String title; // 타이틀
    private String content; // 콘텐츠
    private LocalDateTime createdAt; //생성 일자

}
