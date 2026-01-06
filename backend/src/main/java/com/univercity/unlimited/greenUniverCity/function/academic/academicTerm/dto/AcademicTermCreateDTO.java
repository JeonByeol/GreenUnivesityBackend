package com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicTermCreateDTO {
    @Min(value = 1000, message = "년도는 최소 1000 이상이어야 합니다.")
    @Max(value = 9999, message = "년도는 최대 9999 이하이어야 합니다.")
    private int year;

    @NotBlank(message = "학기를 입력해주세요.")
    @Pattern(regexp = "^(1|2|여름|겨울)학기$",
            message = "학기는 1학기, 2학기, 여름학기, 겨울학기 중 하나여야 합니다.")
    private String semester;

    @NotNull(message = "학기 시작 일자를 입력해주세요.")
    private LocalDate registrationStart;

    @NotNull(message = "학기 종료 일자를 입력해주세요.")
    private LocalDate registrationEnd;

    @Pattern(regexp = "^(?i)(true|false)" ,
    message = "true 혹은 false를 입력해주세요.")
    private Boolean isCurrent;
}