package com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class AttendanceCreateDTO {
    @NotNull(message = "id는 필수 값입니다.")
    private Long enrollmentId; //수강내역 id

    @NotNull(message = "출석 시간은 필수 입니다.")
    private LocalDate attendanceDate; //출석 시간

    @NotNull(message = "상태값은 필수입니다.")
    private AttendanceStatus status; //출,결석 확인

    private Integer week; // 주차 (입력 없으면 날짜 기준 자동 계산)
}
