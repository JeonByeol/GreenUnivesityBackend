package com.univercity.unlimited.greenUniverCity.function.offering.service;

import com.univercity.unlimited.greenUniverCity.function.offering.dto.CourseOfferingDTO;

import java.util.List;
import java.util.Optional;

public interface CourseOfferingService {
    Optional<List<CourseOfferingDTO>> findAllCourseOfferingDTO();

    int addCourseOffering(CourseOfferingDTO courseOfferingDTO);
}
