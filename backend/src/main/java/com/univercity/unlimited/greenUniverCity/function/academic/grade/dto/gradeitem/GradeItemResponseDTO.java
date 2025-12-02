package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GradeItemResponseDTO {
    //GradeItem 정보
    private Long itemId; // 항목 ID

    private String itemName; // 항목명 ex(중간고사,기말고사,과제1)

    private String itemType; //유형

    private int maxScore; // 만점 기준 ex(100,94)

    private int weightPercent; // 반영 비율 ex(30 -> 30%)
    
    //CourseOffering 정보
    private Long offeringId; // 개설강의 id
}

