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


    /**
     *  R-4)Rs에 선언된 myReviewUpdate의 RSL에서 enrollmentId(수강내역Id) 외래키를 활용해서
     *  본인이 작성한 리뷰의 정보를 가져와 수정하기 위한 쿼리
     *  주석된 이유: 리뷰를 수정 할 때 Enroll에 대한 id값을 찾을 필요 없이 바로 ReviewId로 기존에 존재하는 테이블 
     *  칼럼만 찾아서 수정하면 되기 때문에 필요가 없는 상황 하지만 추후 문제가 생기면 필요하다고 생각하고 사용 할 수 있기 때문에 주석처리했음
     *  //Optional<Review> findByEnrollment_enrollmentId(Long enrollmentId);
     */

}
