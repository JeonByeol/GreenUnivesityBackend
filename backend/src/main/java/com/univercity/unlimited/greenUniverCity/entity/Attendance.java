package com.univercity.unlimited.greenUniverCity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

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
    private LocalDate localDate;

    @Column(name="status",nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name="enrollment_id")
    private Enrollment enrollment;
}



