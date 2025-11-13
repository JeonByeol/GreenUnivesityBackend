package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GradeDTO {
    private Integer gradeId;

    private Long offeringId;

    private String gradeValue;

    @JsonBackReference("user-grade")
    private UserDTO user;
}
