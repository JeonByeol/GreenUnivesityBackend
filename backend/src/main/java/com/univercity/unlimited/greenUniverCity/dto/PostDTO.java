package com.univercity.unlimited.greenUniverCity.dto;

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
    private Board board;
    private UserVo user;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private int viewCount;

//    private List<Comment> comments = new ArrayList<>();
}
