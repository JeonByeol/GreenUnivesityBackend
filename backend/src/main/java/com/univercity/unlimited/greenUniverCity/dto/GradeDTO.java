package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference("enrollment-grade")
    @ToString.Exclude
    private EnrollmentDTO enrollment;
}
