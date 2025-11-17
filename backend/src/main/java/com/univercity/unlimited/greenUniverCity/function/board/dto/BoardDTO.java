package com.univercity.unlimited.greenUniverCity.function.board.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BoardDTO {
    private Long boardId;

    private String boardName;

    @Builder.Default
    @JsonManagedReference("board-post")
    private List<PostDTO> posts=new ArrayList<>();

//    private List<Post> posts = new ArrayList<>();
}
