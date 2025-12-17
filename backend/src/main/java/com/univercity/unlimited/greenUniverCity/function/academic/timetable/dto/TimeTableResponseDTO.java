package com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class TimeTableResponseDTO {
    //TimeTable 필드에 담긴 정보
    private Long timetableId; //시간표 아이디

    private DayOfWeek dayOfWeek; //요일

    private LocalTime startTime; //시작시간

    private LocalTime  endTime; // 종료시간

    // 강의실 정보
    private Long classroomId; //강의실 id

    private String classroomName; //Classroom 엔티티의 'location' 값을 담음

    //ClassSection 필드에 담긴 정보
    private String sectionName; //분반명

    private Long sectionId;// 분반 id

    private String courseName; //강의명



}
