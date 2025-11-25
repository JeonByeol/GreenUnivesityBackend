package com.univercity.unlimited.greenUniverCity.function.review.service;

import com.univercity.unlimited.greenUniverCity.function.review.dto.ReviewCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.review.dto.LegacyReviewDTO;
import com.univercity.unlimited.greenUniverCity.function.review.dto.ReviewResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.review.dto.ReviewUpdateDTO;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface ReviewService {
    // 주석-1) ReviewController=R

    //R-1) R에 선언된 postmanTestReview의 요청을 받아서 리뷰 테이블에 존재하는 전체 데이터 조회에 필요한 데이터를 조회하기 위해 동작하는 서비스 선언
    List<LegacyReviewDTO> findAllReview();

    //R-2) R에 선언된 postmanTestStudent의 요청을 받아서 특정 과목에 대해 존재하는 리뷰 목록 조회하기 위해 동작하는 서비스 선언
    List<ReviewResponseDTO> findOfferingForReview(Long offeringId);

    //R-3) R에 선언된 postmanCreateReview의 요청을 받아서 학생이 수강중이거나 완료한 과목에 대한 리뷰를 작성하기 위해 동작하는 서비스 선언
    ReviewResponseDTO createReviewStudent(ReviewCreateDTO dto, String studentEmail);;

    //R-4) R에 선언된 postmanUpdateReview의 요청을 받아서 학생이 기존에 본인이 작성한 리뷰를 수정하기 위해 동작하는 서비스 선언
    ReviewResponseDTO myReviewUpdate(Integer reviewId, ReviewUpdateDTO dto, String studentEmail);

    //R-5) R에 선언된 postmanDeleteReview의 요청을 받아서 학생이 작성한 리뷰를 삭제하기 위해 동작하는 서비스 선언
    void deleteByReview(Integer reviewId,String studentEmail);

    //R-A) **(기능 작성 부탁드리거나/삭제 부탁드립니다) **
    ResponseEntity<String> addReview(LegacyReviewDTO legacyReviewDTO);
}
