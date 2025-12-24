package com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionCreateDTO {

    @NotNull(message = "과제 ID는 필수입니다")
    private Long assignmentId; // 어떤 과제에 대한 제출인지

    @NotBlank(message = "제출 파일 경로는 필수입니다")
    private String fileUrl;    // 업로드된 파일 경로
}