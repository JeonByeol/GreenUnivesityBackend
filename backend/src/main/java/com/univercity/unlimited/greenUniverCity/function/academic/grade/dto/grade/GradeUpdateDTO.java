package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeUpdateDTO {
   private Integer gradeId;//어떤 성적을 수정할지 식별
    private String gradeValue; // 수정할 성적
    private Double score; // 수정할 점수
}
