package com.univercity.unlimited.greenUniverCity.function.academic.grade.entity;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import jakarta.persistence.*;
import lombok.*;

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
    private Integer gradeId; // 성적id

    @Column(name = "grade_value", length = 5)
    private String gradeValue; //학점(등급)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment; //enrollment

    //@Column(name = "score", nullable = false)  12/01 추가 써야함
    //private String totalScore; //총점(95.0)상세점수 합계

    //private Integer gpa // 평점(3.1,4.0,4.5)

    //※ 분반(ClassSection)과 직접 연결되지 않고, 학생의 수강 내역(Enrollment)에 종속된다
}