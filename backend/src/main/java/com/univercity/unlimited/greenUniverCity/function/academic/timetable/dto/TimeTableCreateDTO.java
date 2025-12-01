package com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeTableCreateDTO {
    private Long offeringId; //개설강의고유Id

    private String dayOfWeek; // 요일

    private LocalDateTime startTime; //시작시간

    private LocalDateTime endTime; //종료시간

    private String location; //강의실


}
