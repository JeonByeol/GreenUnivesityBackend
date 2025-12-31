package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class EnrollmentCreateDTO {
    private LocalDateTime enrollDate;//해당 날짜
    private Long sectionId; //분반id
    private Long userId;//유저 식별코드
}
