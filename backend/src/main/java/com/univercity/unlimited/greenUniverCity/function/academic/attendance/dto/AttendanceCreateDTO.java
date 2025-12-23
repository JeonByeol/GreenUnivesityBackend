package com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto;

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
    private String status; //출,결석 확인

}
