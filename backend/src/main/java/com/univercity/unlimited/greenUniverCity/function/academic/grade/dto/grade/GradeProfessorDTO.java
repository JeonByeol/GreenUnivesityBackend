package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeProfessorDTO {
    //교수용
    
    //성적정보
    private Integer gradeId; //점수 아이디
    private String gradeValue; //등급 
    private Double score; // 점수
    
    //과목정보
    private String courseId; //과목코드
    
    private String courseName;//과목명
    
    private String studentName; //학생이름
}
