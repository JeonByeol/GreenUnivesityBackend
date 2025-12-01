package com.univercity.unlimited.greenUniverCity.function.academic.offering.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CourseOfferingUpdateDTO {
    //** 임시 DTO 완성본 아닙니다 **
    private Long offeringId; // 강의 개설 아이디

    private Long professorId; // 담당 교수 아이디
    private String courseName; // 개설 강의명

    private int year; // 개설 년도
    private int semester; // 개설 학기 ex) 1학기 2학기
}
