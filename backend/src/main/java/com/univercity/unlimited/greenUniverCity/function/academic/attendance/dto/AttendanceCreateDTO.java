package com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class AttendanceCreateDTO {
    private Long enrollmentId; //수강내역 id

    private LocalDate attendanceDate; //출석 시간

    private String status; //출,결석 확인

}
