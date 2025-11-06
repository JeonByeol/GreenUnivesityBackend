package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseOfferingRepository extends JpaRepository<CourseOffering, Long> {
    @Query("SELECT DISTINCT c FROM CourseOffering o JOIN o.course c LEFT JOIN FETCH c.offerings WHERE c.courseId = :courseId")
    Optional<Course> findCourseByCourseId(@Param("courseId") Long courseId);
}