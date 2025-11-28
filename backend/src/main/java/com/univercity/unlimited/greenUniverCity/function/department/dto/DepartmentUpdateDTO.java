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

public class DepartmentUpdateDTO {
    //** 임시 DTO 완성본 아닙니다 **
    private Long departmentId;
    private String deptName; // 학과명

}
