package com.univercity.unlimited.greenUniverCity.function.review.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentDTO;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class ReviewDTO {
    private Integer reviewId; //리뷰 아이디

    private Integer rating; //리뷰 점수

    private String comment; //리뷰 코멘트

    private LocalDateTime createdAt; //개시날짜
    
    private LocalDateTime updatedAt; //수정날짜

    @JsonBackReference("enrollment-review")
    @ToString.Exclude
    private EnrollmentDTO enrollmentDTO;
}
