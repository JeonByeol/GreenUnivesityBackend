package com.univercity.unlimited.greenUniverCity.function.offering.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.function.course.dto.LegacyCourseDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.LegacyEnrollmentDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class LegacyCourseOfferingDTO {
    //사용안하고 삭제하기 위한 Legacy 표시입니다

    private Long offeringId; // 개설 강의 ID
    private String professorName; // 담당 교수 ID
    private String courseName;

    private int year; // 개설 년도
    private int semester; // 개설 학기 ex) 1학기 2학기

    @JsonBackReference("course-offering")
    @ToString.Exclude
    private LegacyCourseDTO course; // 강의 정보

    @JsonBackReference("user-offering")
    @ToString.Exclude
    private UserDTO user;

    @Builder.Default
    @JsonManagedReference("offering-enrollment")
    private List<LegacyEnrollmentDTO> enrollments = new ArrayList<>();

//    @Builder.Default
//    @JsonManagedReference("offering-timetable")
//    private List<TimeTableDTO> timeTables = new ArrayList<>();
}
