package com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionResponseDTO {

    private Long submissionId;

    private Long assignmentId;
    private String assignmentTitle; // 과제 제목 (구분용)

    private Long studentId;
    private String studentName;     // 학생 이름
    private String studentNumber;   // 학번 (교수 확인용)

    private String fileUrl;
    private Float score;
    private LocalDateTime submittedAt; // 제출 시각
}
