package com.univercity.unlimited.greenUniverCity.function.grade.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeProfessorDTO {
        private Integer gradeId; //점수 아이디
        private String gradeValue; //점수 벨류

        private String courseId; //과목코드
        private String studentName; //학생이름
}
