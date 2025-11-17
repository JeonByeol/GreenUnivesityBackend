package com.univercity.unlimited.greenUniverCity.function.comment.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class CommentDTO {
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
