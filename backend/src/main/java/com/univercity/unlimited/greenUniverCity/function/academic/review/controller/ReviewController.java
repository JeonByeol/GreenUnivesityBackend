package com.univercity.unlimited.greenUniverCity.function.academic.review.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.ReviewCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.LegacyReviewDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.ReviewResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.ReviewUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/review")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    //R-1) 리뷰 전체 조회
    @GetMapping("/all")
    public List<LegacyReviewDTO> postmanTestReview(){
        log.info("1) 여기는 리뷰 전체 조회 Controller 입니다");
        return reviewService.findAllReview();
    }

    //R-2) 특정 과목에 대해 존재하는 리뷰 목록 조회 컨트롤러
    @GetMapping("/course/{offeringId}")
    public List<ReviewResponseDTO> postmanTestStudent(@PathVariable("offeringId") Long offeringId){
        return reviewService.findOfferingForReview(offeringId);
    }

    //R-3) 학생이 수강중이거나 완료한 과목에 대한 리뷰를 작성하는 컨트롤러
    @PostMapping("/create")
    public ResponseEntity<ReviewResponseDTO> postmanCreateReview(
            @Valid @RequestBody ReviewCreateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String studentEmail) {  // Spring Security에서 이메일 가져옴

        log.info("받은 DTO: {}", dto);

        log.info("1) 리뷰 작성 요청 - 학생: {}, enrollmentId: {}", studentEmail, dto.getEnrollmentId());

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (studentEmail == null || studentEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            studentEmail = "alice@aaa.com"; // 테스트용 기본값
        }

        ReviewResponseDTO response = reviewService.createReviewStudent(dto, studentEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //R-4) 학생이 기존에 작성했던 리뷰에 대해 수정하는 컨트롤러
    @PutMapping("/update/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> postmanUpdateReview(
            @PathVariable("reviewId") Integer reviewId,
            @RequestBody ReviewUpdateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String studentEmail){

        log.info("1) 리뷰 수정 요청 reviewId-:{},comment:{},rating:{}",
                reviewId,dto.getComment(),dto.getRating());

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (studentEmail == null || studentEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            studentEmail = "edward@aaa.com"; // 테스트용 기본값
        }

        ReviewResponseDTO updateReview=reviewService.myReviewUpdate(
                reviewId,
                dto,
                studentEmail
        );

        return ResponseEntity.ok(updateReview);
    }

    //R-5) 학생이 작성한 리뷰를 삭제하기 위해 컨트롤러 내에 선언된 crud
    @DeleteMapping("/delete/{reviewId}")
    public void postmanDeleteReview(
            @PathVariable("reviewId") Integer reviewId,
            @RequestHeader(value = "X-User-Email", required = false) String studentEmail){

        log.info("1) 리뷰 삭제 요청 reviewId-:{},studentEmail:{}",
                reviewId,studentEmail);

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (studentEmail == null || studentEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            studentEmail = "edward@aaa.com"; // 테스트용 기본값
        }

        reviewService.deleteByReview(reviewId,studentEmail);
    }

}

