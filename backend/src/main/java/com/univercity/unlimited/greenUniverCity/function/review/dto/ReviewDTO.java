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
    private Integer reviewId;

    private Integer rating;

    private String comment;

    private LocalDateTime createAt;

    @JsonBackReference("enrollment-review")
    @ToString.Exclude
    private EnrollmentDTO enrollmentDTO;
}
