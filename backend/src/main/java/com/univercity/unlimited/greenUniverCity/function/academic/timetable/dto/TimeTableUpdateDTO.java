package com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeTableUpdateDTO {
    @NotNull(message = "시간표 ID는 필수입니다.")
    private Long timetableId;

    @NotNull(message = "요일은 필수입니다.")
    private DayOfWeek dayOfWeek; // String → DayOfWeek

    @NotNull(message = "시작시간은 필수입니다.")
    private LocalTime startTime; // LocalDateTime → LocalTime

    @NotNull(message = "종료시간은 필수입니다.")
    private LocalTime endTime; //LocalDateTime → LocalTime

    @NotNull(message = "강의실 ID는 필수입니다.")
    private Long classroomId; // 추가


}
