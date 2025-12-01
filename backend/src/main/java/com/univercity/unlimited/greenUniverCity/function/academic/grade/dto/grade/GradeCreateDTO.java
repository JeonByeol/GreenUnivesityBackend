package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeCreateDTO {
    //** 임시 DTO 완성본 아닙니다 **
    private Long enrollmentId; //수강내역 Id
    private String gradeValue; //성적(A+,A,B)

    //** 필요한 추가 필드 **
    private Double score;//점수(0~100)
    private String semester; //학기 정보(2024-1/2024-2)
}
