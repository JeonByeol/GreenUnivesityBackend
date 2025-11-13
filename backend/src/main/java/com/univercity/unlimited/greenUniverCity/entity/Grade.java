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

//    @Column(name = "offering_id")
//    private Long offeringId;
    @Column(name = "grade_value", length = 5)
    private String gradeValue;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude
//    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    @ToString.Exclude
    private Enrollment enrollment;

}