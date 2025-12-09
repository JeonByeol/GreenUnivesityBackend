package com.univercity.unlimited.greenUniverCity.function.academic.grade.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.StudentScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentScoreRepository extends JpaRepository<StudentScore,Long> {
    //특정 수강신청에서 한 학생의 모든 점수를 조회하기 위한 쿼리문
    @Query("SELECT ss FROM StudentScore ss " +
            "JOIN FETCH ss.gradeItem gi " +
            "WHERE ss.enrollment.enrollmentId = :enrollmentId")
    List<StudentScore> findByEnrollmentId(@Param("enrollmentId") Long enrollmentId);

    //특정 평가항목의 모든 학생에 대한 점수를 조회하기 위한 쿼리문
    @Query("SELECT ss FROM StudentScore ss " +
            "JOIN FETCH ss.enrollment e " +
            "JOIN FETCH e.user u " +
            "WHERE ss.gradeItem.itemId = :itemId")
    List<StudentScore> findByItemId(@Param("itemId")Long itemId);

    //특정 학생의 특정 평가항목에 대한 점수 개수 조회 (평가기준에 설정된 모든 점수가 들어오기 전에 등급을 산출하는것을 막기 위해서 사용)
    Long countByEnrollment_EnrollmentId(Long enrollmentId);

    //중복 점수 등록 확인
    boolean existByEnrollment_EnrollmentIdAndGradeItem_ItemId(Long enrollmentId,Long ItemId);

}
