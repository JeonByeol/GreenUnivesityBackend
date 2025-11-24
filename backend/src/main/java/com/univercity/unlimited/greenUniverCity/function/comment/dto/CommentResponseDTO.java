package com.univercity.unlimited.greenUniverCity.function.comment.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostDTO;
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

    //User ** 추후 유라님이 필요한 칼럼 작성하실 예정 **
    private String writerName;//작성자 이름

    //Post (?) ** 추후 유라님이 필요한 칼럼 작성하실 예정 **
    private String name; //게시글 이름(?) ** 확실한 역할 작성해주시거나/삭제 부탁드립니다 **


}
