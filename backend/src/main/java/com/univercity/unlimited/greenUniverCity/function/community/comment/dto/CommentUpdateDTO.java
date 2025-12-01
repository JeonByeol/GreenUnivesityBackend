package com.univercity.unlimited.greenUniverCity.function.community.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class CommentUpdateDTO {
    //** 추후 유라님이 필요한 칼럼 작성하실 예정 ** 완성본 아닙니다
    private Long commentId;
    private String title;
    private String content; //댓글내용
    private LocalDateTime createdAt;//작성일시
    private Long postId;



}
