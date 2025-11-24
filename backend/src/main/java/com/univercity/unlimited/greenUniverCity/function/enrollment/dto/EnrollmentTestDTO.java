package com.univercity.unlimited.greenUniverCity.function.enrollment.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class EnrollmentTestDTO {
    private Long enrollmentId; // 과목 코드

//    private Long studentId;// 학생 식별정보(user)

    private String studentName;//학생이름(user)

    private Long offeringId;// 개설 강의 식별정보(offering)

    private String courseName;// 강의명(course)
}
