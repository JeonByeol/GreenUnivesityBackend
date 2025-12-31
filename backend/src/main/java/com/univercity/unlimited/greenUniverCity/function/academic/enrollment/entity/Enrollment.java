package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "tbl_enrollment",
        uniqueConstraints = {
                //복합 UNIQUE 제약조건: 같은 학생이 같은 분반에 2번 이상 신청 불가
                @UniqueConstraint(
                        name = "uk_enrollment_user_section",
                        columnNames = {"user_id", "section_id"}
                )
        }
)
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
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user; //유저 
    
    //12-02 추가 CoursOffering->ClassSection으로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    @ToString.Exclude
    private ClassSection classSection; //분반
}
