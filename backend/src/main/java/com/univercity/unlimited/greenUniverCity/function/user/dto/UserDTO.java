package com.univercity.unlimited.greenUniverCity.function.user.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.comment.dto.CommentDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.notice.dto.NoticeDTO;
import com.univercity.unlimited.greenUniverCity.function.offering.dto.CourseOfferingDTO;
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
    private Long userId; //유저 아이디

    private String email; //이메일
    private String password; //패스워드
    private String nickname; //닉네임

    private String  role; //롤


    @Builder.Default
    @JsonManagedReference("user-offering")
    private List<CourseOfferingDTO> offerings=new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("user-enrollment")
    private List<EnrollmentDTO> enrollments=new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("user-comment")
    private List<CommentDTO> comments=new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("user-post")
    private List<PostDTO> posts=new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("user-notice")
    private List<NoticeDTO> notices=new ArrayList<>();

}
