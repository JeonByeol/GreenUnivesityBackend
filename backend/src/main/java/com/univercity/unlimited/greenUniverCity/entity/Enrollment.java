package com.univercity.unlimited.greenUniverCity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "tbl_enrollment")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long enrollmentId; // 과목 코드

    @Column(name = "enroll_date")
    private LocalDateTime enrollDate;

    @ManyToOne
    @JoinColumn(name = "offering_id")
    @ToString.Exclude
    private CourseOffering courseOffering;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserVo userVo;
}
