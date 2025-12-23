package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    // -- 전체 Entity --

    //E-2/E-4)Enrollment에 대한 id를 찾음/다른 service에서 enroll에대한 id값을 알기 위한 쿼리 선언
    Enrollment findByEnrollmentId(Long id);


    //추가: 중복 수강신청 검사
    /**
     * 특정 학생이 특정 분반에 이미 수강신청했는지 확인
     *
     * @param userId 학생 ID
     * @param sectionId 분반 ID
     * @return 존재 여부 (true: 중복, false: 신청 가능)
     */
    boolean existsByUserUserIdAndClassSectionSectionId(Long userId, Long sectionId);

    //추가: 특정 학생의 특정 분반 수강신청 내역 조회
    @Query("SELECT e FROM Enrollment e " +
            "WHERE e.user.userId = :userId " +
            "AND e.classSection.sectionId = :sectionId")
    List<Enrollment> findByUserIdAndSectionId(
            @Param("userId") Long userId,
            @Param("sectionId") Long sectionId
    );
}
