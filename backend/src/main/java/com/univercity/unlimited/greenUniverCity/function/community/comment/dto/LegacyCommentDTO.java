package com.univercity.unlimited.greenUniverCity.function.community.comment.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class LegacyCommentDTO {
    //사용안하고 삭제하기 위한 Legacy 표시입니다

    private Long commentId;

    private String content;
//    private String writerName;
    private LocalDateTime createdAt;
//    private String name;
    @JsonBackReference("user-comment")
    @ToString.Exclude
    private UserDTO user;

    @JsonBackReference("post-comment")
    @ToString.Exclude
    private PostDTO posts;




}
