package com.univercity.unlimited.greenUniverCity.function.grade.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentDTO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GradeDTO {
    private Integer gradeId;

    private String gradeValue;

    @JsonIgnore
    private String courseName;

    @JsonBackReference("enrollment-grade")
    @ToString.Exclude
    private EnrollmentDTO enrollment;
}
