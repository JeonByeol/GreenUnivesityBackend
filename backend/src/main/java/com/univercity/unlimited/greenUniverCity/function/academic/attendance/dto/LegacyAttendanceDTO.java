package com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.LegacyEnrollmentDTO;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class LegacyAttendanceDTO {
    //사용안하고 삭제하기 위한 Legacy 표시입니다

    private Long attendanceId; //출석아이디

    private LocalDate attendanceDate; //출석 시간

    private String status; //출,결석 확인

    @JsonBackReference("enrollment-attendance")
    @ToString.Exclude
    private LegacyEnrollmentDTO enrollment; //수강내역
}
