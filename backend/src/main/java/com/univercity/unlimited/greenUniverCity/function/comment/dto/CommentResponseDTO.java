package com.univercity.unlimited.greenUniverCity.function.comment.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.function.post.entity.Post;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserDTO;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class CommentResponseDTO {
    //Comment
    private Long commentId; //댓글 고유 ID

    private String content; //댓글내용

    private LocalDateTime createdAt;//작성일시

    private LocalDateTime updatedAt; //수정날짜

    private Long postId;

    private String writerName;//작성자 이름

    private String boardName;


}
