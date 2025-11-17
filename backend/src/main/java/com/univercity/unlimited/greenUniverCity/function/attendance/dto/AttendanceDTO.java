package com.univercity.unlimited.greenUniverCity.function.attendance.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentDTO;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class AttendanceDTO {
    private Integer attendanceId; //출석아이디

    private LocalDateTime localDateTime; //출석 시간

    private String status; //출,결석 확인

    @JsonBackReference("enrollment-attendance")
    @ToString.Exclude
    private EnrollmentDTO enrollment; //수강내역
}
