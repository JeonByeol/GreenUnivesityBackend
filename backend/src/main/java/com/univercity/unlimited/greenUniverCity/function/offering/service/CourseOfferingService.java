package com.univercity.unlimited.greenUniverCity.function.offering.service;

import com.univercity.unlimited.greenUniverCity.function.offering.dto.CourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;

import java.util.List;
import java.util.Optional;

public interface CourseOfferingService {
    Optional<List<CourseOfferingDTO>> findAllCourseOfferingDTO();

    int addCourseOffering(CourseOfferingDTO courseOfferingDTO);


//    CourseOfferingDTO findByCourseNameForTimeTable(String courseName);//CO-3) Timetable에 강의명을 넘겨주기 위해 구성한 service
}
