package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GradeDTO {
    private Integer gradeId;
    private String gradeValue;
    @JsonBackReference("enrollment-grade")
    private EnrollmentDTO enrollment;
}
