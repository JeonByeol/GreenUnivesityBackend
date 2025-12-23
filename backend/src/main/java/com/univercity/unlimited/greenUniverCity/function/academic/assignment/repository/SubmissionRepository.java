package com.univercity.unlimited.greenUniverCity.function.academic.assignment.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    // 전체조회 관련 쿼리문
    @Query("SELECT s FROM Submission s " +
            "JOIN FETCH s.student st " +
            "JOIN FETCH s.assignment a " +
            "JOIN FETCH a.classSection cs " + // 과제가 어느 반 것인지도 보통 필요함
            "ORDER BY s.submissionId ASC")
    List<Submission> findAllWithFetchJoin();
    
    // SB-4) 특정 과제의 모든 제출물 조회 (교수용)
    // - 학생 정보와 과제 정보를 Fetch Join하여 성능 최적화
    @Query("SELECT s FROM Submission s " +
            "JOIN FETCH s.student st " +
            "JOIN FETCH s.assignment a " +
            "WHERE a.assignmentId = :assignmentId " +
            "ORDER BY s.submittedAt DESC")
    List<Submission> findByAssignmentId(@Param("assignmentId") Long assignmentId);

    // SB-5) 특정 과제에 대한 특정 학생의 제출물 조회 (중복 확인 및 내역 조회용)
    @Query("SELECT s FROM Submission s " +
            "JOIN FETCH s.student st " +
            "JOIN FETCH s.assignment a " +
            "WHERE a.assignmentId = :assignmentId AND st.email = :email")
    Optional<Submission> findByAssignmentIdAndStudentEmail(@Param("assignmentId") Long assignmentId, @Param("email") String email);

    //성적 산출용: 특정 강의(Offering)에서 특정 학생이 받은 과제 점수 총합 조회
    // - COALESCE(..., 0): 점수가 없으면 NULL 대신 0.0을 반환
    // - 경로: Submission -> Assignment -> ClassSection -> CourseOffering
    @Query("SELECT COALESCE(SUM(s.score), 0) FROM Submission s " +
            "WHERE s.assignment.classSection.courseOffering.offeringId = :offeringId " +
            "AND s.student.email = :email")
    Float sumTotalScoreByOfferingAndStudent(@Param("offeringId") Long offeringId, @Param("email") String email);
}