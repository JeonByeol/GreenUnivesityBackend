package com.univercity.unlimited.greenUniverCity.function.enrollment.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.attendance.dto.AttendanceDTO;
import com.univercity.unlimited.greenUniverCity.function.offering.dto.CourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.function.review.dto.ReviewDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.grade.dto.GradeDTO;
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

public class EnrollmentDTO {
    private Long enrollmentId;// 과목 코드

    private LocalDateTime enrollDate;//해당 날짜

    @JsonBackReference("offering-enrollment")
    @ToString.Exclude
    private CourseOfferingDTO courseOffering;//강의 개설

    @JsonBackReference("user-enrollment")
    @ToString.Exclude
    private UserDTO user;//유저

    @Builder.Default
    @JsonManagedReference("enrollment-grade")
    private List<GradeDTO> grades=new ArrayList<>();//성적 모음

    @Builder.Default
    @JsonManagedReference("enrollment-attendance")
    private List<AttendanceDTO> attendances= new ArrayList<>();//출석 모음

    @Builder.Default
    @JsonManagedReference("enrollment-review")
    private List<ReviewDTO> reviews = new ArrayList<>();//리뷰 모음


}
