package com.univercity.unlimited.greenUniverCity.function.offering.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.course.dto.LegacyCourseDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.LegacyEnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CourseOfferingCreateDTO {
    //** 임시 DTO 완성본 아닙니다 **

    private Long courseId;//강의 식별 아이디
    private String professorName; // 담당 교수 이름
    private String courseName; //강의명

    private int year; // 개설 년도
    private int semester; // 개설 학기 ex) 1학기 2학기

}
