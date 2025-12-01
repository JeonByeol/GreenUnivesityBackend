package com.univercity.unlimited.greenUniverCity.function.academic.course.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.LegacyCourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.function.member.department.dto.LegacyDepartmentDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LegacyCourseDTO {
    //사용안하고 삭제하기 위한 Legacy 표시입니다
    private Long courseId; // 과목 코드

    private String courseName; // 과목명

    private String description; // 강의 설명

    private Integer credits; // 학점

    @JsonBackReference("dept-course")
    @ToString.Exclude
    private LegacyDepartmentDTO department; // 학과

    @Builder.Default
    @JsonManagedReference("course-offering")
    private List<LegacyCourseOfferingDTO> offerings = new ArrayList<>();

}

