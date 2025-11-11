package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.entity.Board;
import com.univercity.unlimited.greenUniverCity.entity.Comment;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import lombok.*;

import java.time.LocalDate;
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
    private Long postId;

    private String title;

    private String content;

    private LocalDateTime createAt;

    private int viewCount;

    @JsonBackReference("board-post")
    private BoardDTO board;

    @JsonBackReference ("user-post")
    private UserDTO user;

    @Builder.Default
    @JsonManagedReference("post-comment")
    private List<CommentDTO> comments=new ArrayList<>();

}
