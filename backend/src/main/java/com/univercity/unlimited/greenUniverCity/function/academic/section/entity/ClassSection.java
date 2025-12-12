package com.univercity.unlimited.greenUniverCity.function.academic.section.entity;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "tbl_class_section")
@ToString
public class ClassSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long sectionId; // 분반 ID

    @Column(name = "section_name", nullable = false, length = 20)
    private String sectionName; // 분반명 (예: A반, 주간반)

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity; // 정원 (예: 40)

    // 핵심 어떤 강의의 분반인지 연결 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id", nullable = false)
    @ToString.Exclude // Lombok 무한 루프 방지
    private CourseOffering courseOffering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    @ToString.Exclude
    private Classroom classroom;
}
