package com.univercity.unlimited.greenUniverCity.function.academic.grade.entity;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_grade") // 최종성적 테이블
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@ToString
public class Grade{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long gradeId; // 성적id

    @Column(name = "total_score", nullable = false)
    private Float totalScore;//총점

    @Column(name = "letter_grade", nullable = false, length = 2)
    private String letterGrade;//등급

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;//생성일자
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;//수정일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;//수강신청

    //총점을 기반으로 등급을 계산하는 함수
    public static String calculateGrade(Float score) {
        if (score >= 95.0) return "A+";
        if (score >= 90.0) return "A";
        if (score >= 85.0) return "B+";
        if (score >= 80.0) return "B";
        if (score >= 75.0) return "C+";
        if (score >= 70.0) return "C";
        if (score >= 65.0) return "D+";
        if (score >= 60.0) return "D";
        return "F";
    }

    //총점을 기반으로 등급을 계산하는 함수를 받아서 자동으로 등급을 생성하는 함수
    public void setTotalScoreAndCalculateGrade(Float totalScore) {
        this.totalScore = totalScore;
        this.letterGrade = calculateGrade(totalScore);
    }
}