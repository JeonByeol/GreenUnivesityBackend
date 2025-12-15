package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentScoreCreateDTO {

    @NotNull(message = "등록 ID는 필수입니다")
    private Long enrollmentId;//수강신청Id

    @NotNull(message = "평가항목 ID는 필수입니다")
    private Long itemId;//평가기준Id

    @NotNull(message = "획득 점수는 필수입니다")
    @DecimalMin(value = "0.0", message = "획득 점수는 0 이상이어야 합니다")
    private Float scoreObtained;//흭득점수
}
