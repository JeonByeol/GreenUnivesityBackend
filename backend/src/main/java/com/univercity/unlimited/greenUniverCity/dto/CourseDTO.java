package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.entity.Department;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Long courseId; // 과목 코드

    private String courseName; // 과목명

    private String description; // 강의 설명

    private Integer credits; // 학점

    @JsonBackReference("dept-course")
    private DepartmentDTO department; // 학과

    @Builder.Default
    @JsonManagedReference("course-offering")
    private List<CourseOfferingDTO> offerings = new ArrayList<>();

}

