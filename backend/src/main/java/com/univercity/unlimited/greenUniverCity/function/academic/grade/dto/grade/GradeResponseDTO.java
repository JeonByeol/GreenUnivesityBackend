package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GradeResponseDTO {
    //Grade
    private Long gradeId;// 성적Id

    private Float totalScore; //총점

    private String letterGrade;//등급

    private LocalDateTime createdAt;//생성시간

    private LocalDateTime updatedAt;//수정시간\

    //Enrollment
    private Long enrollmentId;// 수강신청Id

    //User
    private String studentName;//학생이름

    //CourseOffering
    private String courseName;//과목명
}
