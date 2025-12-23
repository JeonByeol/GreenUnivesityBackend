package com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity;

import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "tbl_submission")
@ToString
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long submissionId; // 제출 ID

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt; // 제출 시각

    @Column(name = "score")
    private Float score; // 획득 점수 (채점 결과)

    @Column(name = "file_url", length = 500, nullable = false)
    private String fileUrl; // 파일 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    @ToString.Exclude
    private Assignment assignment; // 과제 연결 (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private User student; // 학생(User) 연결 (FK)

    // 편의 메서드: 채점
    public void gradeSubmission(Float score) {
        this.score = score;
    }

    // 편의 메서드: 파일 재제출 (수정)
    public void updateFile(String fileUrl) {
        this.fileUrl = fileUrl;
        this.submittedAt = LocalDateTime.now(); // 제출 시간 갱신
    }
}
