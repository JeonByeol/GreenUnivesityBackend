package com.univercity.unlimited.greenUniverCity.function.academic.offering.entity;

import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_course_offering")// 개설강의 테이블
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

//    @Column(name = "professor_name")
//    private String professorName; // 담당 교수 이름

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "year")
    private int year; // 개설 년도

    @Column(name = "semester")
    private String semester; // 개설 학기 ex) 1학기 2학기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
//    @ToString.Exclude
    private Course course; // 강의 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
//    @ToString.Exclude
    private User professor;

//    @OneToMany(mappedBy = "courseOffering", fetch = FetchType.LAZY)
//    @Builder.Default
//    @ToString.Exclude
//    private List<Enrollment> enrollments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "courseOffering", fetch = FetchType.LAZY)
//    @Builder.Default
//    @ToString.Exclude
//    private List<TimeTable> timeTables = new ArrayList<>();

//    public void addEnrollment(Enrollment enrollment) {
//        enrollments.add(enrollment);
//    }
//
//    public void addTimeTable(TimeTable timeTable) {
//        timeTables.add(timeTable);
//    }
}
