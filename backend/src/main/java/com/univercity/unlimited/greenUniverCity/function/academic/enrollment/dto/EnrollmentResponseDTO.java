package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class EnrollmentResponseDTO {
    //** 임시 DTO 완성본 아닙니다 **

    //Enrollment
    private Long enrollmentId;

    private LocalDateTime enrollDate;//해당 날짜

    private Long offeringId; //강의 개설id



    //User 테이블에 필요한 DTO를 채우거나 사용 안하면 삭제 예정
    private Long userId;//유저 식별코드

    private Long sectionId;

}
