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

public class DepartmentResponseDTO {
    //** 임시 DTO 완성본 아닙니다 **

    //Department
    private Long departmentId; //학과고유Id

    private String deptName; // 학과명

    //Course
//    private Long courseId;//과목코드
}
