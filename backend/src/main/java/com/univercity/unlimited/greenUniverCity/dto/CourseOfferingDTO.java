package com.univercity.unlimited.greenUniverCity.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CourseOfferingDTO {
    private Long offeringId; // 개설 강의 ID
    private String professorName; // 담당 교수 ID
    private String courseName;

    private int year; // 개설 년도
    private int semester; // 개설 학기 ex) 1학기 2학기

    @JsonBackReference("course-offering")
    private CourseDTO course; // 강의 정보

    @JsonBackReference("user-offering")
    private UserDTO user;

    @Builder.Default
    @JsonManagedReference("offering-enrollment")
    private List<EnrollmentDTO> enrollments = new ArrayList<>();

    @Builder.Default
    @JsonManagedReference("offering-timetable")
    private List<TimeTableDTO> timeTables = new ArrayList<>();
}
