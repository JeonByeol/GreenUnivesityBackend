package com.univercity.unlimited.greenUniverCity.function.academic.course.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCreateDTO {
    @NotBlank(message = "과목명을 입력해주세요.")
    private String courseName; // 과목명

    @NotBlank(message = "강의 설명을 입력해주세요.")
    private String description; // 강의 설명

    @NotNull(message = "학점을 입력해주세요.")
    @Min(value = 1, message = "학점은 1 이상이어야 합니다.")
    @Max(value = 4, message = "학점은 4 이하이어야 합니다.")
    private Integer credits; // 학점

    @NotNull(message = "학과 ID를 입력해주세요.")
    private Long departmentId; // 학과 코드
}

