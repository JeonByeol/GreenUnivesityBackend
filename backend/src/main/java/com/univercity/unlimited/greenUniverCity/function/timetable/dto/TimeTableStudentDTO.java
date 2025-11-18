package com.univercity.unlimited.greenUniverCity.function.timetable.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.function.offering.dto.CourseOfferingDTO;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class TimeTableStudentDTO {
    private Integer timetableId; //수강표 아이디

    private String dayOfWeek; //이번주의 오늘

    private LocalDateTime startTime; //시작 타임

    private LocalDateTime endTime; // 끝 타임

    private String location; // 돌림

    private String courseName;

}
