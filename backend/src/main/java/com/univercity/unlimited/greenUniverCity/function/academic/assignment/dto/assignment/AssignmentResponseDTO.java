package com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentResponseDTO {

    private Long assignmentId;
    private Long sectionId;       // 분반 ID
    private String sectionName;   // 분반 이름 (화면 표시용)

    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Float maxScore;

    // 생성일자 등 필요한 경우 추가
    // private LocalDateTime createdAt;
}