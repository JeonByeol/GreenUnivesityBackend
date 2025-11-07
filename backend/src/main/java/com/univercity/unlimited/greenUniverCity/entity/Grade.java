package com.univercity.unlimited.greenUniverCity.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_grade")
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
    private Integer gradeId;

    @Column(name = "grade_value", length = 5)
    private String gradeValue;

    @OneToOne(mappedBy = "grade", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Enrollment enrollment;
}