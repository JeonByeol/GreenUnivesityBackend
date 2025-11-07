package com.univercity.unlimited.greenUniverCity.dto;


import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import jakarta.persistence.*;
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
    private String professorId; // 담당 교수 ID

    private int year; // 개설 년도

    private int semester; // 개설 학기 ex) 1학기 2학기

    private CourseDTO course; // 강의 정보

    private UserDTO user;


    private List<EnrollmentDTO> enrollments = new ArrayList<>();


    private List<TimeTableDTO> timeTables = new ArrayList<>();
}
