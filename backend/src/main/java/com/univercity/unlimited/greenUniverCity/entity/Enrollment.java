package com.univercity.unlimited.greenUniverCity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_enrollment")
@ToString
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id")
    @ToString.Exclude
    private CourseOffering courseOffering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserVo user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    @ToString.Exclude
    private Grade grade;

    @OneToMany(mappedBy = "enrollment",fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "enrollment", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

    public void addGrade(Grade grade) {
        this.setGrade(grade);
    }
    public void addAttendance(Attendance attendance){
        attendances.add(attendance);
    }
    public void addReview(Review review) {
        reviews.add(review);
    }
}
