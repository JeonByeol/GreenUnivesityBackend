package com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicTermCreateDTO {
    private int year;
    private String semester;
    private LocalDate registrationStart;
    private LocalDate registrationEnd;
    private boolean isCurrent;
}