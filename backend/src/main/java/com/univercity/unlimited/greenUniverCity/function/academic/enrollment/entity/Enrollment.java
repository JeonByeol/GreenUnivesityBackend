package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_enrollment") //수강신청 테이블
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
    private LocalDateTime enrollDate; //신청 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id")
//    @ToString.Exclude
    private CourseOffering courseOffering; //강의 개설 ClassSection 등장으로 삭제 예정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//    @ToString.Exclude
    private User user; //유저 
    
    //12-02 추가 CoursOffering->ClassSection으로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    @ToString.Exclude
    private ClassSection classSection; //분반
}
