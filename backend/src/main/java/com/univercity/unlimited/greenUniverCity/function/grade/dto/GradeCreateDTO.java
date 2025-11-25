package com.univercity.unlimited.greenUniverCity.function.grade.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeCreateDTO {
    //** 임시 DTO 완성본 아닙니다 **
    private Long enrollmentId; //수강내역 Id
    private String gradeValue; // 점수
}
