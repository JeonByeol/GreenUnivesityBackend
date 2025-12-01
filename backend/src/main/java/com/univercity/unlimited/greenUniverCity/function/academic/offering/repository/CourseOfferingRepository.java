package com.univercity.unlimited.greenUniverCity.function.academic.offering.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseOfferingRepository extends JpaRepository<CourseOffering,Long> {
    //CO-3)CourseOffering에 대한 id를 찾음/다른 service에서 enroll에대한 id값을 알기 위한 쿼리 선언
    CourseOffering findByOfferingId(Long id);
}