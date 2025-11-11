package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.entity.Course;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
