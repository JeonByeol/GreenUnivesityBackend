package com.univercity.unlimited.greenUniverCity.function.community.review.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.LegacyEnrollmentDTO;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class LegacyReviewDTO {
    //사용안하고 삭제하기 위한 Legacy 표시입니다

    private Integer reviewId; //리뷰 아이디

    private Integer rating; //리뷰 점수

    private String comment; //리뷰 코멘트

    private LocalDateTime createdAt; //개시날짜
    
    private LocalDateTime updatedAt; //수정날짜

    @JsonBackReference("enrollment-review")
    @ToString.Exclude
    private LegacyEnrollmentDTO legacyEnrollmentDTO;
}
