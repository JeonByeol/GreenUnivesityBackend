package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private UserDTO user;

    @JsonBackReference("post-comment")
    private PostDTO posts;




}
