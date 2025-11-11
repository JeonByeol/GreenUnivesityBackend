package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GradeDTO {
    private Integer gradeId;

    private Long enrollmentId;

    private String gradeValue;

    @JsonBackReference("user-grade")
    private UserDTO user;
}
