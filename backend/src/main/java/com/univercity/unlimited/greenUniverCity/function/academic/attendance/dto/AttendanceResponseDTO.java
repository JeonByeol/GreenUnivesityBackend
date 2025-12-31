package com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.AttendanceStatus;
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

    private AttendanceStatus status; //출,결석 확인

    private Integer week; // 주차

    //Enrollment
    private Long enrollmentId; //수강내역 id

    //User 
    private String studentNickName; //학생이름

    //CourseOffering
    private String courseName; //과목명
    
    
    //프론트앤드 조회 전용
    public String getWeekString() {
        return week + "주차";
    }
}
