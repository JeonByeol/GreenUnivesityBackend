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
    private Long enrollmentId; // 과목 코드

    private LocalDateTime enrollDate;

    @JsonBackReference("offering-enrollment")
    @ToString.Exclude
    private CourseOfferingDTO courseOffering;

    @JsonBackReference("user-enrollment")
    @ToString.Exclude
    private UserDTO user;

    @Builder.Default
    @JsonManagedReference("enrollment-grade")
    private List<GradeDTO> grades=new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("enrollment-attendance")
    private List<AttendanceDTO> attendances= new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("enrollment-review")
    private List<ReviewDTO> reviews = new ArrayList<>();


}
