package com.univercity.unlimited.greenUniverCity.function.member.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class UserDTO {
    //사용안하고 삭제하기 위한 Legacy 표시입니다

    private Long userId; //유저 아이디

    private String email; //이메일
    private String password; //패스워드
    private String nickname; //닉네임

    private String  role; //역할(권한)

}
