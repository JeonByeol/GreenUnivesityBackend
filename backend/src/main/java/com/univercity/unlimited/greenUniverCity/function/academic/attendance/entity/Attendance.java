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

    @Column(name="status",nullable = false)
    private String status; //출결상태(출석,지각,조퇴)

    @ManyToOne
    @JoinColumn(name="enrollment_id")
    private Enrollment enrollment;//수강내역Id

//    @Column(name="week",nullable = false) 12/01 추가 써야함
//    private String week; // 주차(1주,2주,3주,...,14주..)

}



