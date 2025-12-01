package com.univercity.unlimited.greenUniverCity.function.academic.grade.repository;


import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade,Integer> {
    //G-2)학생이 본인이 수강한 모든 과목의 성적과 과목명을 조회하기 위한 쿼리
    @Query("SELECT g FROM Grade g " +
            "JOIN FETCH g.enrollment e " +
            "JOIN FETCH e.courseOffering co " +
            "JOIN FETCH e.user u " +
            "WHERE u.email = :email")
    List<Grade> findByMyGrade(@Param("email")String email);

    //G-3)교수가 특정 과목의 수업을 듣는 전체학생 조회하기 위한 쿼리
    @Query("SELECT g FROM Grade g " +
            "JOIN FETCH g.enrollment e " +
            "JOIN FETCH e.user u " +
            "WHERE e.courseOffering.offeringId =:offeringId " +
            "ORDER BY u.nickname ASC")
    List<Grade> findByOfferingGrade(@Param("offeringId") Long offeringId);

    //G-4) 교수가 학생에 대한 정보를 받아와서 성적의 대한 값을 수정하기 위한 쿼리
    Optional<Grade> findByEnrollment_enrollmentId(Long enrollmentId);




}
