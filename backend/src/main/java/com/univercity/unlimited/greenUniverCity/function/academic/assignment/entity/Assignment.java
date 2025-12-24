package com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity;

import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "tbl_assignment")
@ToString
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId; // 과제 ID

    @Column(name = "title", nullable = false, length = 100)
    private String title; // 과제명

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 과제 설명

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate; // 마감 기한

    @Column(name = "max_score", nullable = false)
    private Float maxScore; // 만점

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    @ToString.Exclude // 무한 루프 방지
    private ClassSection classSection; // 분반 연결 (FK)



    // 편의 메서드: 과제 정보 수정
    public void updateAssignment(String title, String description, LocalDateTime dueDate, Float maxScore) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (dueDate != null) this.dueDate = dueDate;
        if (maxScore != null) this.maxScore = maxScore;
    }
}