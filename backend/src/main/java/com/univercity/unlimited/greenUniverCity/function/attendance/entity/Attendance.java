package com.univercity.unlimited.greenUniverCity.function.attendance.entity;

import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import jakarta.persistence.*;
import lombok.*;

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
    private Integer attendanceId;

    @Column(name="date",nullable = false)
    private LocalDateTime localDateTime;

    @Column(name="status",nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name="enrollment_id")
    private Enrollment enrollment;
}



