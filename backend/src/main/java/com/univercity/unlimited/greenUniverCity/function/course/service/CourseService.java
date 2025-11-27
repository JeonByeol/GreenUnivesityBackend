package com.univercity.unlimited.greenUniverCity.function.course.service;

import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.course.dto.LegacyCourseDTO;

import java.util.List;
import java.util.Map;

public interface CourseService {
    List<LegacyCourseDTO> legacyFindAllCourse();
    List<CourseResponseDTO> findAllCourse();
    List<CourseResponseDTO> findById(Long courseId);
    CourseResponseDTO createCourseByAuthorizedUser(CourseCreateDTO dto, String email);
    CourseResponseDTO updateCourseByAuthorizedUser(CourseUpdateDTO dto, String email);
    Map<String,String> deleteByCourseId(Long courseId, String email);
//    CourseDTO findByCourseNameForTimeTable(Long id);//C-3) Timetable에 강의명을 넘겨주기 위해 구성한 service
}
