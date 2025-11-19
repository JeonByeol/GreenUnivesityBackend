package com.univercity.unlimited.greenUniverCity.function.review.repository;

import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.grade.entity.Grade;
import com.univercity.unlimited.greenUniverCity.function.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    //주석-1) ReviewService=RS
    //주석-2) ReviewServiceImpl=RSL

    //R-2)RS에 선언된 findCourseForReview의 RSL에서 offeringId(개성강의Id) 외래키를 활용해서 User의 이름 Course의 개설강의 정보를 끌어옴
    //특정 과목에 대해 존재하는 리뷰 목록 조회 쿼리 식별
    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.enrollment e " +
            "JOIN FETCH e.courseOffering co " +
            "JOIN FETCH e.user u " +
            "WHERE e.courseOffering.offeringId = :offeringId")
    List<Review> findReviewsByCourseOfferingId(@Param("offeringId") Long offeringId);

    //R-3)RS에 선언된 writeReviewStudent의 RSL에서 중복 리뷰를 확인하는
    //validateDuplicateReview함수의 체크용 쿼리
    boolean existsByEnrollment(Enrollment enrollment);

    //R-4)Rs에 선언된 myReviewUpdate의 RSL에서 enrollmentId(수강내역Id) 외래키를 활용해서
    //본인이 작성한 리뷰의 정보를 가져와 수정하기 위한 쿼리
    Optional<Review> findByEnrollment_enrollmentId(Long enrollmentId);
}
