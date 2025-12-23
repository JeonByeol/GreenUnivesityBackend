package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItemType;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GradeItemResponseDTO {
    //GradeItem 정보
    private Long itemId;//평가항목 Id

    private String itemName;//항목명 ex(중간고사,기말고사,과제1)

    private GradeItemType itemType;//유형

    private Float maxScore;//만점 기준 ex(100,94)

    private Float weightPercent; //반영 비율 ex(30 -> 30%)

    private LocalDateTime createdAt;//생성시간

    private LocalDateTime updatedAt;//수정시간

    
    //CourseOffering 정보
    private Long offeringId;// 개설강의 id
    
    private String courseName;//과목명

    //User
    private Integer studentCount;//학생 점수
}

