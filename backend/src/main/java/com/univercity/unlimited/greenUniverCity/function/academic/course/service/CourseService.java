package com.univercity.unlimited.greenUniverCity.function.academic.course.service;

import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;

import java.util.List;
import java.util.Map;

public interface CourseService {
    List<CourseResponseDTO> legacyFindAllCourse();
    List<CourseResponseDTO> findAllCourse();
    List<CourseResponseDTO> findById(Long courseId);
    CourseResponseDTO createCourseByAuthorizedUser(CourseCreateDTO dto, String email);
    CourseResponseDTO updateCourseByAuthorizedUser(CourseUpdateDTO dto, String email);
    Map<String,String> deleteByCourseId(Long courseId, String email);

    Course getByCourseId(Long courseId);
//    CourseDTO findByCourseNameForTimeTable(Long id);//C-3) Timetable에 강의명을 넘겨주기 위해 구성한 service
}
