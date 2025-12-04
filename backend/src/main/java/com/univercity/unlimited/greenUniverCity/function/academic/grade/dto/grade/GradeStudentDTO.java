package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeStudentDTO {
    private Integer gradeId; //점수 아이디
    private String gradeValue; //점수 벨류

    private String courseName; // 강의이름
    private Long courseId;    //과목코드
    private String studentName; //학생이름
}
