package com.univercity.unlimited.greenUniverCity.function.academic.offering.dto;


import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CourseOfferingCreateDTO {
    private String courseName; // 개설 강의명
    private Long courseId;//강의 식별 아이디
    private Long professorId; // 담당 교수 아이디
    private Long termId;
}