package com.univercity.unlimited.greenUniverCity.function.attendance.entity;

import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="attendance_id")
    private Long attendanceId; //출결고유 Id

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate; //출결날짜

    @Column(name="status",nullable = false)
    private String status; //출결상태(출석,지각,조퇴)

    @ManyToOne
    @JoinColumn(name="enrollment_id")
    private Enrollment enrollment;//수강내역Id
}



