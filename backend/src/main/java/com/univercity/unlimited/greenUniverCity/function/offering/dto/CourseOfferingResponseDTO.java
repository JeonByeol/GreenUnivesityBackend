package com.univercity.unlimited.greenUniverCity.function.offering.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.course.dto.LegacyCourseDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.LegacyEnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    private Long professorId; // 담당 교수 아이디
    private String professorName; // 담당 교수 이름
    private String courseName; // 개설 강의 이름

    private int year; // 개설 년도
    private int semester; // 개설 학기 ex) 1학기 2학기

    //Course 테이블에 필요한 DTO를 채우거나 사용 안하면 삭제 예정
    private Long courseId;

    //Enrollment 테이블에 필요한 DTO를 채우거나 사용 안하면 삭제 예정

    //User 테이블에 필요한 DTO를 채우거나 사용 안하면 삭제 예정

}
