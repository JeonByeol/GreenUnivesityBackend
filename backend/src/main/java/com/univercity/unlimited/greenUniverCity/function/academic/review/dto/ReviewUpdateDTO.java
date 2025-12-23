package com.univercity.unlimited.greenUniverCity.function.academic.review.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateDTO {

    @NotNull(message = "id는 필수 값입니다")
    private Long reviewId;

    /**
     * 수정할 수강평 내용
     */
    @NotBlank(message = "수강평을 입력해주세요.")
    @Size(min = 10, max = 500, message = "수강평은 10자 이상 500자 이하로 작성해주세요.")
    private String comment;

    /**
     * 수정할 평점 (1~5)
     */
    @NotNull(message = "평점을 입력해주세요.")
    @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5점 이하여야 합니다.")
    private Integer rating;
}