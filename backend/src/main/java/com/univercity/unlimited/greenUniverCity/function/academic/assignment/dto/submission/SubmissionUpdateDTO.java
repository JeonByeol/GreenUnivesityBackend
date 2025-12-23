package com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionUpdateDTO {

    @NotNull(message = "제출 ID는 필수입니다")
    private Long submissionId; // 제출 ID (필수)

    // 학생이 파일을 다시 낼 때
    private String fileUrl;

    // 교수가 채점할 때 (0점 이상)
    @Min(value = 0, message = "점수는 0점 이상이어야 합니다")
    private Float score;
}