package com.univercity.unlimited.greenUniverCity.dto.grade;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeStudentDTO {
    private Integer gradeId;
    private String gradeValue;

    private String courseName; // 강의이름
    private Long courseId;    //과목코드
    private String studentName; //학생이름
}
