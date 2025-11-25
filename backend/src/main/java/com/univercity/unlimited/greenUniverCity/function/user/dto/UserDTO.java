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

public class UserDTO {
    //사용안하고 삭제하기 위한 Legacy 표시입니다

    private Long userId; //유저 아이디

    private String email; //이메일
    private String password; //패스워드
    private String nickname; //닉네임

    private String  role; //롤


    @Builder.Default
    @JsonManagedReference("user-offering")
    private List<LegacyCourseOfferingDTO> offerings=new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("user-enrollment")
    private List<LegacyEnrollmentDTO> enrollments=new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("user-comment")
    private List<LegacyCommentDTO> comments=new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("user-post")
    private List<PostDTO> posts=new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("user-notice")
    private List<NoticeDTO> notices=new ArrayList<>();

}
