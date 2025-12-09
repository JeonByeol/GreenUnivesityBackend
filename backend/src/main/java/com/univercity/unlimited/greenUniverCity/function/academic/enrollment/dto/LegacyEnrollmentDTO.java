package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.LegacyAttendanceDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.LegacyCourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.function.community.review.dto.LegacyReviewDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.LegacyGradeDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class LegacyEnrollmentDTO {
    //사용안하고 삭제하기 위한 Legacy 표시입니다

    private Long enrollmentId;// 과목 코드

    private LocalDateTime enrollDate;//해당 날짜

    @JsonBackReference("offering-enrollment")
    @ToString.Exclude
    private LegacyCourseOfferingDTO courseOffering;//강의 개설

    @JsonBackReference("user-enrollment")
    @ToString.Exclude
    private UserDTO user;//유저

    @Builder.Default
    @JsonManagedReference("enrollment-grade")
    private List<LegacyGradeDTO> grades=new ArrayList<>();//성적 모음

    @Builder.Default
    @JsonManagedReference("enrollment-attendance")
    private List<LegacyAttendanceDTO> attendances= new ArrayList<>();//출석 모음

    @Builder.Default
    @JsonManagedReference("enrollment-review")
    private List<LegacyReviewDTO> reviews = new ArrayList<>();//리뷰 모음


}
