package com.univercity.unlimited.greenUniverCity.function.grade.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.LegacyEnrollmentDTO;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GradeResponseDTO {
    private Integer gradeId;
    private String gradeValue;
    private Double score;
    private Long enrollmentId;
    private String studentName;
    private String courseName;
    private String message;           // "성적이 등록되었습니다" 등
    private LocalDateTime createdAt;
}
