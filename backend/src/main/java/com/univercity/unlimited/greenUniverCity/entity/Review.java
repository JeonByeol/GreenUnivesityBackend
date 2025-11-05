package com.univercity.unlimited.greenUniverCity.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(exclude = "enrollment")
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
    private Integer reviewId;

    @Column(name = "rating", nullable = false)
    private Integer rating;


    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

}