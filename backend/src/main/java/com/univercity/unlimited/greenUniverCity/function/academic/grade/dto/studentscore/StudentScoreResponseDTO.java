package com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentScoreResponseDTO {
    //StudentScore
    private Long scoreId;//상세점수Id

    private Float scoreObtained;//흭득 점수

    private LocalDateTime createdAt;//생성시간

    private LocalDateTime updatedAt;//수정시간

    //GradeItem
    private Long itemId;//평가기준Id

    private String itemName;//항목명(중간,기말)

    private Float maxScore;//만점기준

    private Float weightPercent;//반영비율

    //User
    private String studentName;//학생이름

    //Enrollment
    private Long enrollmentId;
    
    //추가정보
    private Float weightedScore; // 가중치 적용 점수

}
