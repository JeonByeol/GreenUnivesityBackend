package com.univercity.unlimited.greenUniverCity.function.member.department.dto;

import lombok.*;

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
