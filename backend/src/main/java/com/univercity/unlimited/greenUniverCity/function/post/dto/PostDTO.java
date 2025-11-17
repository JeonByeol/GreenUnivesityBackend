package com.univercity.unlimited.greenUniverCity.function.post.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.board.dto.BoardDTO;
import com.univercity.unlimited.greenUniverCity.function.comment.dto.CommentDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class PostDTO {
    private Long postId; //포스트 아이디

    private String title; // 타이틀

    private String content; //콘텐츠

    private LocalDateTime createAt; //게시날짜

    private int viewCount; // 뷰 수

    @JsonBackReference("board-post")
    @ToString.Exclude
    private BoardDTO board;

    @JsonBackReference ("user-post")
    @ToString.Exclude
    private UserDTO user;

    @Builder.Default
    @JsonManagedReference("post-comment")
    private List<CommentDTO> comments=new ArrayList<>();

}
