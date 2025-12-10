package com.univercity.unlimited.greenUniverCity.function.academic.review.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class ReviewResponseDTO {
    private Integer reviewId; //리뷰 아이디

    private Integer rating; //리뷰 점수

    private String comment; //리뷰 코멘트

    private LocalDateTime createdAt; //개시날짜

    @JsonInclude(JsonInclude.Include.NON_NULL) //null이면 JSON에서 제외
    private LocalDateTime updatedAt;//수정날짜

    private String courseName; //강의 이름

    private String studentNickname; //학생
}
