package com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class AttendanceUpdateDTO {
    private Long attendanceId;

    private LocalDate attendanceDate; //출석 시간

    private String status; //출,결석 확인

}
