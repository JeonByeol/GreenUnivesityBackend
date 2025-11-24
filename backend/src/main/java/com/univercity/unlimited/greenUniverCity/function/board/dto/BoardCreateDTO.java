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
public class BoardCreateDTO {
    //** 추후 유라님이 필요한 칼럼 작성하실 예정 ** 완성본 아닙니다

    private String boardName; //게시판 이름
}
