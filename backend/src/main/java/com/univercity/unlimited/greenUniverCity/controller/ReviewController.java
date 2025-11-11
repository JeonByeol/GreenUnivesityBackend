package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.dto.ReviewDTO;
import com.univercity.unlimited.greenUniverCity.entity.Review;
import com.univercity.unlimited.greenUniverCity.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/review")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/all")
    public List<ReviewDTO> postmanTestReview(){
        log.info("여기는 리뷰 전체 조회 Controller 입니다");
        return reviewService.findAllReview();

    }
}
