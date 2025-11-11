package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import com.univercity.unlimited.greenUniverCity.entity.Review;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentDTO {
    private Long enrollmentId; // 과목 코드

    private LocalDateTime enrollDate;

    @JsonBackReference("offering-enrollment")
    private CourseOfferingDTO courseOffering;

    @JsonBackReference("user-enrollment")
    private UserDTO user;

//    @JsonManagedReference("enrollment-grade")
//    private GradeDTO grade;

    @Builder.Default
    @JsonManagedReference("enrollment-attendance")
    private List<AttendanceDTO> attendances= new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("enrollment-review")
    private List<ReviewDTO> reviews = new ArrayList<>();




}
