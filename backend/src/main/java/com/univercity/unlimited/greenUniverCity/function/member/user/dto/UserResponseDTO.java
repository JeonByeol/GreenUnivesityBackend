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
    private String role; //권한(역할)
    private String studentNumber; // 학번
    private String deptName; // 학과 번호
    private String password;
}
