package com.univercity.unlimited.greenUniverCity.function.academic.review.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    //주석-1) ReviewService=RS
    //주석-2) ReviewServiceImpl=RSL

    //R-2)RS에 선언된 findOfferingForReview의 RSL에서 offeringId(개성강의Id) 외래키를 활용해서 User의 이름 Course의 개설강의 정보를 끌어와서 사용하기 위해 선언된 쿼리
    //특정 과목에 대해 존재하는 리뷰 목록 조회 쿼리 식별
    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.enrollment e " +
            "JOIN FETCH e.classSection se " +
            "JOIN FETCH e.user u " +
            "JOIN FETCH se.courseOffering co " +
            "WHERE co.offeringId = :offeringId")
    List<Review> findReviewsByCourseOfferingId(@Param("offeringId") Long offeringId);

    //R-3)RS에 선언된 writeReviewStudent의 RSL에서 중복 리뷰를 확인하기 위해 선언된
    //validateDuplicateReview 함수에서 EnrollmentId의 값을 활용하여 데이터 조회를 시켜주기 위해 선언된 쿼리
    boolean existsByEnrollment(Enrollment enrollment);

    // 기존 findAll()을 덮어쓰거나 새로운 이름으로 만듭니다.
    // 리뷰 + 수강내역 + 학생 + 분반 + 강의개설정보 + 과목정보를 한 번에 가져옴
    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.enrollment e " +
            "JOIN FETCH e.user u " +
            "JOIN FETCH e.classSection cs " +
            "JOIN FETCH cs.courseOffering co " +
            "JOIN FETCH co.course c")
    List<Review> findAllWithDetails();
}
