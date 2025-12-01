package com.univercity.unlimited.greenUniverCity.function.member.department.entity;


import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_department") //학과 테이블
@ToString
@Getter
@Setter
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
