package com.univercity.unlimited.greenUniverCity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;


@Entity
@Table(name = "tbl_course")
@ToString
@Getter
public class Course {
    @Id
    @Column(name = "course_id")
    private String deptId; // 과목 코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department; // 학과

    @Column(name = "course_name")
    private String courseName; // 과목명

    private String description; // 강의 설명
    private Integer credits; // 학점
}
