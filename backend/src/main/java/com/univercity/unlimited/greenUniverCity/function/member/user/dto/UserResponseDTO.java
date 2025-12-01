package com.univercity.unlimited.greenUniverCity.function.member.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class UserResponseDTO {

    //응답용DTO
    private Long userId; //유저 아이디
    private String email; //이메일
    private String nickname; //닉네임
    private String  role; //권한(역할)

//    private String password; //패스워드 ** 비밀번호는 절대로 화면에 보이면 안됨 ** 삭제 예정

}
