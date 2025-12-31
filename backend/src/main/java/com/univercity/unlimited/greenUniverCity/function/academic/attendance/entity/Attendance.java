package com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString

@Table(
       name="tbl_attendance",indexes={
               //외래키enrollment_id
        @Index(columnList = "enrollment_id",name="idx_attendance_enrollment")
        } 
) // 출결 엔티티
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="attendance_id")
    private Long attendanceId; //출결고유 Id

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate; //출결날짜

    @Column(name="status", nullable = false)
    @Enumerated(EnumType.STRING) // DB에는 "PRESENT", "LATE" 문자열로 저장됨
    private AttendanceStatus status;

    @ManyToOne
    @JoinColumn(name="enrollment_id")
    private Enrollment enrollment;//수강내역Id

    @Column(name="week",nullable = false)
    private Integer week; //주차

}



