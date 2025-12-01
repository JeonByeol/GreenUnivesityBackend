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

public class AttendanceResponseDTO {
    //Attendance
    private Long attendanceId; //출석아이디

    private LocalDate attendanceDate; //출석 시간

    private String status; //출,결석 확인

    //Enrollment
    private Long enrollmentId; //수강내역 id

    //User 
    private String studentNickName; //학생이름
}
