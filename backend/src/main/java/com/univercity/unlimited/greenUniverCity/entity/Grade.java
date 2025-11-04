package com.univercity.unlimited.greenUniverCity.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@ToString(exclude = "enrollment")

@Table(
        name = "tbl_grade", indexes = {
        @Index(columnList = "enrollment_id", name = "idx_grade_enrollment")
    }
)
public class Grade{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Integer gradeId;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    @Column(name = "grade_value", length = 5)
    private String gradeValue;


}