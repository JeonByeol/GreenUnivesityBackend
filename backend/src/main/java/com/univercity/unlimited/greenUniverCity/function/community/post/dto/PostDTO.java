package com.univercity.unlimited.greenUniverCity.function.community.post.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.LegacyBoardDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.LegacyCommentDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserDTO;
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
    // p-1) postDTO 생성
    private Long postId; //포스트 아이디

    private String title; // 타이틀

    private String content; //콘텐츠

    private LocalDateTime createAt; //게시날짜

    private int viewCount; // 뷰 수

    private String PostId;

    @JsonBackReference("board-post")
    @ToString.Exclude
    private LegacyBoardDTO board;

    @JsonBackReference ("user-post")
    @ToString.Exclude
    private UserDTO user;

    @Builder.Default
    @JsonManagedReference("post-comment")
    private List<LegacyCommentDTO> comments=new ArrayList<>();

}
