package com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "출결 ID는 필수입니다.")
    private Long attendanceId;

    @NotNull(message = "출결 상태는 필수입니다.")
    private String status;

    @NotNull(message = "날짜는 필수입니다.") // [추가] 이제 null이 허용되지 않습니다.
    private LocalDate attendanceDate;

}
