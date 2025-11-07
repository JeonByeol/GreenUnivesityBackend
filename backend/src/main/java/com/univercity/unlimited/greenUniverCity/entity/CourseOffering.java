package com.univercity.unlimited.greenUniverCity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_course_offering")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseOffering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offering_id")
    private Long offeringId; // 개설 강의 ID

    @Column(name = "professor_id")
    private String professorId; // 담당 교수 ID

    @Column(name = "year")
    private int year; // 개설 년도

    @Column(name = "semester")
    private int semester; // 개설 학기 ex) 1학기 2학기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    private Course course; // 강의 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserVo user;

    @OneToMany(mappedBy = "courseOffering", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "courseOffering", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<TimeTable> timeTables = new ArrayList<>();

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    public void addTimeTable(TimeTable timeTable) {
        timeTables.add(timeTable);
    }
}
