package com.univercity.unlimited.greenUniverCity.function.review.dto;

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

    private LocalDateTime createdAt; //개시 날짜

    private String courseName; //강의 이름

    private String studentNickname; //학생
}
