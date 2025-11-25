package com.univercity.unlimited.greenUniverCity.function.course.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseUpdateDTO {
    //** 임시 DTO 완성본 아닙니다 **
    private String courseName; // 과목명

    private String description; // 강의 설명

    private Integer credits; // 학점

}

