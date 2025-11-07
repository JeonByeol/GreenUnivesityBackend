package com.univercity.unlimited.greenUniverCity.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_department")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId; // 학과 ID

    @Column(name = "dept_name")
    private String deptName; // 학과명

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Course> courses = new ArrayList<>();

    public void addCourse(Course course) {
        courses.add(course);
    }
}
