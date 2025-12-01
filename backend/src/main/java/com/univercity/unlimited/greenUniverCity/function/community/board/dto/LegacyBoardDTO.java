package com.univercity.unlimited.greenUniverCity.function.community.board.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LegacyBoardDTO {
    //사용안하고 삭제하기 위한 Legacy 표시입니다
    private Long boardId;

    private String boardName;

    @Builder.Default
    @JsonManagedReference("board-post")
    private List<PostDTO> posts=new ArrayList<>();

//    private List<Post> posts = new ArrayList<>();
}
