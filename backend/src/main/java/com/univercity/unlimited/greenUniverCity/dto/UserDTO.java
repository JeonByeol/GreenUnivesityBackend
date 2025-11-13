package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.entity.UserRole;
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
    private Long userId;

    private String email;
    private String password;
    private String nickname;

    private String  role;


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
