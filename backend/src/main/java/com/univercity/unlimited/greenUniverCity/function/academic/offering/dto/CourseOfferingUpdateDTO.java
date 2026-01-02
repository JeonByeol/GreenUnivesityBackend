package com.univercity.unlimited.greenUniverCity.function.academic.offering.dto;


import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CourseOfferingUpdateDTO {
    //** 임시 DTO 완성본 아닙니다 **
    private Long offeringId; // 강의 개설 아이디
    private String courseName; // 개설 강의명
    private Long courseId; // 강의 정보
    private Long professorId; // 담당 교수 아이디
    private Long termId;
}
