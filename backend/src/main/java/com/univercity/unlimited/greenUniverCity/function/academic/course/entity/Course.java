package com.univercity.unlimited.greenUniverCity.function.academic.course.entity;

import com.univercity.unlimited.greenUniverCity.function.member.department.entity.Department;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tbl_course") //과목 테이블
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId; // 과목 코드

    @Column(name = "course_name")
    private String courseName; // 과목명

    @Column(name = "description")
    private String description; // 강의 설명

    @Column(name = "credits")
    private Integer credits; // 학점

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
//    @ToString.Exclude
    private Department department; // 학과

//    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
//    @Builder.Default
//    @ToString.Exclude
//    private List<CourseOffering> offerings = new ArrayList<>();
//
//    public void addCourseOffering(CourseOffering courseOffering){
//        offerings.add(courseOffering);
//    }
}