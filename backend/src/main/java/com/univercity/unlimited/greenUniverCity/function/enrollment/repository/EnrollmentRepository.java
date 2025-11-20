package com.univercity.unlimited.greenUniverCity.function.enrollment.repository;

import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    //E-2/E-4)Enrollment에 대한 id를 찾음/다른 service에서 enroll에대한 id값을 알기 위한 쿼리 선언
    Enrollment findByEnrollmentId(Long id);

}
