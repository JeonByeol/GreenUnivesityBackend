package com.univercity.unlimited.greenUniverCity.function.course.service;

import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseDTO;

import java.util.List;

public interface CourseService {
    List<CourseDTO> findAllCourse();

    int addCourse(CourseDTO courseDTO);
}
