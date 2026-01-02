package com.univercity.unlimited.greenUniverCity.function.academic.grade.entity;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_grade_item") //평가 항목 기준 테이블
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@ToString
public class GradeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId; // 항목 ID

    @Column(name = "item_name", nullable = false)
    private String itemName; // 항목명 (예: 중간고사, 기말고사, 과제1)

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private GradeItemType itemType;//유형

    @Column(name = "max_score", nullable = false)
    private Float maxScore; // 만점 기준 (예: 100)

    @Column(name = "weight_percent", nullable = false)
    private Float weightPercent; // 반영 비율 (예: 30 -> 30%)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id")
    @ToString.Exclude // 순환 참조 방지
    private CourseOffering courseOffering; // 해당 강의의 평가 기준

    public void updateGradeItemInfo(String itemName, Float maxScore, Float weightPercent,GradeItemType itemType) {
        if (itemName != null) this.itemName = itemName;
        if (maxScore != null) this.maxScore = maxScore;
        if (weightPercent != null) this.weightPercent = weightPercent;
        if (itemType != null) this.itemType = itemType;
    }
}
