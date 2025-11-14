package com.univercity.unlimited.greenUniverCity.function.grade.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeProfessorDTO {
        private Integer gradeId;
        private String gradeValue;

        private String courseId; //과목코드
        private String studentName; //학생이름
}
