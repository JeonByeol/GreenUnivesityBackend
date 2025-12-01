package com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto;

import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class TimeTableResponseDTO {
    //TimeTable 필드에 담긴 정보
    private Integer timetableId; //시간표 아이디

    private String dayOfWeek; //요일

    private LocalDateTime startTime; //시작시간

    private LocalDateTime endTime; // 종료시간

    private String location; // 장소

    //CourseOffering 필드에 담긴 정보
    private String courseName; //강의명

    private String professorNickname; //학생이름

    private Long offeringId; //개설강의고유 id
}
