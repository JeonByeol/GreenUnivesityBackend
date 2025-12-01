package com.univercity.unlimited.greenUniverCity.function.community.review.entity;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Setter

@Table(
        name = "tbl_review", indexes = {
        @Index(columnList = "enrollment_id", name = "idx_review_enrollment")
}
)
public class Review{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId; //리뷰 아이디

    @Column(name = "rating", nullable = false)
    private Integer rating; //리뷰 점수

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment; //코멘트

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; //개시날짜

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; //수정날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

}