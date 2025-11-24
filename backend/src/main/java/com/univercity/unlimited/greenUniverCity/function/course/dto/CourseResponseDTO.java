package com.univercity.unlimited.greenUniverCity.function.course.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDTO {
    //** 임시 DTO 완성본 아닙니다 **

    //Course
    private Long courseId;//과목코드

    private String courseName; // 과목명

    private String description; // 강의 설명

    private Integer credits; // 학점

    //Department
    private Long departmentId; //학과Id

    //CourseOffering
    private String professorNickName;

}

