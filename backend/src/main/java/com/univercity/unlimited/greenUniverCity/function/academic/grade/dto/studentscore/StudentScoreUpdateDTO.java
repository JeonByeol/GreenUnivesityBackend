package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentScoreUpdateDTO {

    @NotNull(message = "상세점수 Id는 필수입니다.")
    private Long scoreId;

    @NotNull(message = "획득 점수는 필수입니다")
    @DecimalMin(value = "0.0", message = "획득 점수는 0 이상이어야 합니다")
    private Float scoreObtained;
}
