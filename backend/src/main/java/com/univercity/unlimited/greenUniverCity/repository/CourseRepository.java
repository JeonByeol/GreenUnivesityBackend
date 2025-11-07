package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT DISTINCT c FROM Course c JOIN FETCH c.offerings o WHERE o.offeringId = :offeringId")
    Optional<Course> findByOfferingId(@Param("offeringId") Long offeringId);

}
