package com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentUpdateDTO {

    @NotNull(message = "수정할 과제 ID는 필수입니다")
    private Long assignmentId;    // 수정할 과제 ID (필수)

    @Size(min = 1, max = 100, message = "과제명은 1~100자 이내여야 합니다")
    private String title;         // 제목 수정

    @Size(max = 2000, message = "과제 설명은 2000자 이내여야 합니다")
    private String description;   // 설명 수정

    @Future(message = "수정할 마감 기한은 현재 시간보다 미래여야 합니다")
    private LocalDateTime dueDate; // 기한 연장/단축

    @Min(value = 1, message = "만점은 최소 1점 이상이어야 합니다")
    private Float maxScore;       // 만점 수정
}
