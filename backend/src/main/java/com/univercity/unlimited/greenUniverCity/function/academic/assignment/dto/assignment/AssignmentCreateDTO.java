package com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentCreateDTO {

    @NotNull(message = "분반 ID는 필수입니다")
    private Long sectionId;       // 어느 분반에 과제를 낼 것인지

    @NotBlank(message = "과제명은 필수입니다")
    @Size(min = 1, max = 100, message = "과제명은 1~100자 이내여야 합니다")
    private String title;         // 과제 제목

    // 설명은 필수가 아닐 수도 있지만, 길이 제한은 두는 것이 좋습니다.
    @Size(max = 2000, message = "과제 설명은 2000자 이내여야 합니다")
    private String description;   // 과제 설명

    @NotNull(message = "마감 기한은 필수입니다")
    @Future(message = "마감 기한은 현재 시간보다 미래여야 합니다")
    private LocalDateTime dueDate; // 마감 기한

    @NotNull(message = "만점 점수는 필수입니다")
    @Min(value = 1, message = "만점은 최소 1점 이상이어야 합니다")
    private Float maxScore;       // 만점 점수
}