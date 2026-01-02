package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItemType;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class GradeItemUpdateDTO {

    @NotNull(message = "평가항목 Id는 필수입니다")
    private Long itemId;

    @NotBlank(message = "평가 항목명은 필수입니다")
    @Size(min = 1, max = 50, message = "평가 항목명은 1-50자 이내여야 합니다")
    private String itemName; // 중간고사, 기말고사, 과제1

    @NotNull(message = "평가 유형은 필수입니다")
    private GradeItemType itemType;

    @NotNull(message = "만점은 필수입니다")
    @DecimalMin(value = "0.0", inclusive = false, message = "만점은 0보다 커야 합니다")
    private Float maxScore; // 만점 (예: 100)

    @NotNull(message = "반영 비율은 필수입니다")
    @DecimalMin(value = "0.0", message = "반영 비율은 0% 이상이어야 합니다")
    @DecimalMax(value = "100.0", message = "반영 비율은 100% 이하여야 합니다")
    private Float weightPercent; // 반영 비율 (예: 30%)

    @Size(max = 500, message = "설명은 500자 이내여야 합니다")
    private String description; // 평가 항목 설명 (선택사항)
}
