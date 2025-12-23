package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
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
}
