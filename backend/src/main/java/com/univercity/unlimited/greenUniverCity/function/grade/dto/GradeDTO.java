package com.univercity.unlimited.greenUniverCity.function.grade.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.LegacyEnrollmentDTO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GradeDTO {
    private Integer gradeId; //성적 아이디

    private String gradeValue; //성적 벨류

    @JsonIgnore
    private String courseName; // 수강 이름

    @JsonBackReference("enrollment-grade")
    @ToString.Exclude
    private LegacyEnrollmentDTO enrollment; //수강신청내역
}
