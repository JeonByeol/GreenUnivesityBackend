package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class EnrollmentUpdateDTO {
    //** 임시 DTO 완성본 아닙니다 **
    private Long enrollmentId; // 수강 내역 아이디
    private LocalDateTime enrollDate;//해당 날짜
    private Long offeringId;
    private Long userId;
}
