package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    // -- 전체 Entity --

    //E-2/E-4)Enrollment에 대한 id를 찾음/다른 service에서 enroll에대한 id값을 알기 위한 쿼리 선언
    Enrollment findByEnrollmentId(Long id);


    // 특정 학생이 특정 분반에 이미 신청했는지 확인 (True/False 반환)
    boolean existsByUserUserIdAndClassSectionSectionId(Long userId, Long sectionId);
    
    //  특정 강의(Offering)를 수강하는 모든 학생 목록 조회 (성적 일괄 산출용) GradeService에서 사용
    @Query("SELECT e FROM Enrollment e " +
            "JOIN FETCH e.user " + // 학생 정보도 필요할 테니 Fetch Join
            "WHERE e.classSection.courseOffering.offeringId = :offeringId")
    List<Enrollment> findAllByOfferingId(@Param("offeringId") Long offeringId);


    @Query("""
    SELECT DISTINCT cs.courseOffering, e.user
    FROM Enrollment e
    JOIN e.classSection cs
    JOIN cs.courseOffering co
    JOIN co.professor p
    WHERE p.email = :professorEmail
    """)
    List<Object[]> findStudentsWithCourseByProfessorEmail(@Param("professorEmail") String professorEmail);

}
