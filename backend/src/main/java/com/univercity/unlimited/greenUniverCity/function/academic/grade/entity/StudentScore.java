package com.univercity.unlimited.greenUniverCity.function.academic.grade.entity;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_student_score") //평가 항목 기준 테이블
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@ToString
public class StudentScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long scoreId; // 점수 ID

    @Column(name = "score_obtained", nullable = false)
    private Float scoreObtained; // 획득 점수 (예: 85.5)

    @Column(name = "submission_date")
    private LocalDateTime submissionDate; // 제출일/응시일

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;//생성일자

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;//수정일자

    // 핵심 변경 누가(Enrollment) 점수를 받았는가?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    @ToString.Exclude // 순환 참조 방지
    private Enrollment enrollment;

    //핵심 변경 어떤 평가 항목(GradeItem)에 대한 점수인가?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @ToString.Exclude // 순환 참조 방지
    private GradeItem gradeItem;

    public void updateScore(Float scoreObtained) {
        if (scoreObtained != null) {
            this.scoreObtained = scoreObtained;
        }
    }
}
