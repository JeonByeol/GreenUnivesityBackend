package com.univercity.unlimited.greenUniverCity.function.course.service;

import com.univercity.unlimited.greenUniverCity.function.course.dto.LegacyCourseDTO;

import java.util.List;

public interface CourseService {
    List<LegacyCourseDTO> findAllCourse();

    int addCourse(LegacyCourseDTO legacyCourseDTO);

//    CourseDTO findByCourseNameForTimeTable(Long id);//C-3) Timetable에 강의명을 넘겨주기 위해 구성한 service
}
