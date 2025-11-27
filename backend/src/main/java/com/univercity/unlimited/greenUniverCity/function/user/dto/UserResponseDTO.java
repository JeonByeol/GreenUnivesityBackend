package com.univercity.unlimited.greenUniverCity.function.user.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.comment.dto.LegacyCommentDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.LegacyEnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.notice.dto.NoticeDTO;
import com.univercity.unlimited.greenUniverCity.function.offering.dto.LegacyCourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    private String password; //패스워드

}
