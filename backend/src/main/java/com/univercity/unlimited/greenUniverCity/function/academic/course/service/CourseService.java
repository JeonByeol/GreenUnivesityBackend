package com.univercity.unlimited.greenUniverCity.function.academic.course.service;

import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;

import java.util.List;
import java.util.Map;

public interface CourseService {
    List<CourseResponseDTO> findAllCourse();
    CourseResponseDTO findById(Long courseId);
    CourseResponseDTO createCourse(CourseCreateDTO dto);
    CourseResponseDTO updateCourse(CourseUpdateDTO dto);
    Map<String,String> deleteCourse(Long courseId);

    Course getByCourseId(Long courseId);
//    CourseDTO findByCourseNameForTimeTable(Long id);//C-3) Timetable에 강의명을 넘겨주기 위해 구성한 service
}
