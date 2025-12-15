package com.univercity.unlimited.greenUniverCity.function.academic.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreateDTO {
    private Long enrollmentId;
    private Integer rating;
    private String comment;


    // Validation
    @Min(value = 1, message = "평점은 1 이상이어야 합니다")
    @Max(value = 5, message = "평점은 5 이하여야 합니다")
    public Integer getRating() {
        return rating;
    }

    @NotBlank(message = "리뷰 내용을 입력해주세요")
    @Size(min = 10, max = 1000, message = "리뷰는 10자 이상 1000자 이하로 작성해주세요")
    public String getComment() {
        return comment;
    }
}