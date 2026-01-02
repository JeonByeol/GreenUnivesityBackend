package com.univercity.unlimited.greenUniverCity.function.academic.offering.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CourseOfferingResponseDTO {
    //** 임시 DTO 완성본 아닙니다 **
    //CourseOffering
    private Long offeringId; // 개설 강의 ID
    private String courseName; // 개설 강의 이름
    private Long courseId;
    private Long professorId; // 담당 교수 아이디
    private Long termId;
}
