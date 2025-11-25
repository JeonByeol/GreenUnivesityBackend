package com.univercity.unlimited.greenUniverCity.function.offering.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CourseOfferingUpdateDTO {
    //** 임시 DTO 완성본 아닙니다 **
    
    private String professorName; // 담당 교수 이름
    private String courseName; // 강의명

    private int year; // 개설 년도
    private int semester; // 개설 학기 ex) 1학기 2학기
    
}
