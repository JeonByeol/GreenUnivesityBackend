package com.univercity.unlimited.greenUniverCity.function.community.comment.dto;

import com.univercity.unlimited.greenUniverCity.function.community.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    private Long commentId;
    private String content;
    private LocalDateTime createdAt;

    private Long postId;
    private Long userId;
    private String nickname;

    public static CommentResponseDTO from(Comment comment) {
        return CommentResponseDTO.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .postId(comment.getPost() != null ? comment.getPost().getPostId() : null)
                .userId(comment.getUser() != null ? comment.getUser().getUserId() : null)
                .nickname(comment.getUser() != null ? comment.getUser().getNickname() : null)
                .build();
    }
}
