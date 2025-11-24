package com.univercity.unlimited.greenUniverCity.function.course.repository;

import com.univercity.unlimited.greenUniverCity.function.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
//    Course findByCourseId(Long id); C-3) Timetable에 강의명을 넘겨주기 위해 구성한 service

}
