package com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto;

import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicTermResponseDTO {
    private Long termId;
    private int year;
    private String semester;
    private LocalDate registrationStart;
    private LocalDate registrationEnd;
    private boolean isCurrent;
}
