package com.univercity.unlimited.greenUniverCity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "tbl_course")
@ToString
@Getter
public class Enrollment {
    @Id
    @Column(name = "enrollment_id")
    private Long enrollmentId; // 과목 코드

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserVo user;

    @ManyToOne
    @JoinColumn(name = "offering_id")
    private CourseOffering courseOffering;

    @Column(name = "enroll_date")
    private Date enrollDate;
}
