package com.univercity.unlimited.greenUniverCity.function.academic.section.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSectionCreateDTO {
    @NotNull(message = "개설 강의 ID는 필수입니다")
    private Long offeringId;//개설강의id

    @NotBlank(message = "분반명은 필수입니다")
    @Size(min = 1, max = 20, message = "분반명은 1-20자 이내여야 합니다")
    private String sectionName;//분반명

    @NotNull(message = "정원은 필수입니다")
    @Min(value = 1, message = "정원은 최소 1명 이상이어야 합니다")
    @Max(value = 300, message = "정원은 최대 300명 이하여야 합니다")
    private Integer maxCapacity;//정원
}
