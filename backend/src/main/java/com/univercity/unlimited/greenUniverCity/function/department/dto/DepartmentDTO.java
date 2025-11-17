package com.univercity.unlimited.greenUniverCity.function.department.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class DepartmentDTO {
    private Long departmentId; // 학과 ID

    private String deptName; // 학과명

    @Builder.Default
    @JsonManagedReference("dept-course")
    private List<CourseDTO> courses = new ArrayList<>();
}
