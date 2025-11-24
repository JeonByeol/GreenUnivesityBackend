package com.univercity.unlimited.greenUniverCity.function.department.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.course.dto.LegacyCourseDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class LegacyDepartmentDTO {
    //사용안하고 삭제하기 위한 Legacy 표시입니다

    private Long departmentId; // 학과 ID

    private String deptName; // 학과명

    @Builder.Default
    @JsonManagedReference("dept-course")
    private List<LegacyCourseDTO> courses = new ArrayList<>();
}
