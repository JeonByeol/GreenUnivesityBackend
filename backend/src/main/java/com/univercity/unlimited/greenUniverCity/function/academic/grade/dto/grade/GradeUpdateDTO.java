package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeUpdateDTO {

    @NotNull(message = "성적Id는 필수입니다")
    private Long gradeId;

    @NotNull(message = "총점은 필수입니다")
    @DecimalMin(value = "0.0", message = "총점은 0 이상이어야 합니다")
    @DecimalMax(value = "100.0", message = "총점은 100 이하여야 합니다")
    private Float totalScore; //총점

    @NotBlank(message = "등급은 필수입니다")
    @Pattern(regexp = "^(A\\\\+?|B\\\\+?|C\\\\+?|D\\\\+?|F)$", message = "등급 형식이 올바르지 않습니다. (A+, A, B+, B, C+, C, D+, D, F 형식만 허용됩니다.)")
    private String letterGrade; //등급
}
