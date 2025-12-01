package com.univercity.unlimited.greenUniverCity.function.community.board.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BoardUpdateDTO {
    private String boardName; //게시판 이름
    private Long boardId;
}
