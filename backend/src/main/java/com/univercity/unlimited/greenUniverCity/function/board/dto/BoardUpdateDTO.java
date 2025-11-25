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
public class BoardUpdateDTO {
    private String boardName; //게시판 이름

    //ex 테이블 명: )Post ** 추후 유라님이 필요한 칼럼 작성하실 예정 **
}
