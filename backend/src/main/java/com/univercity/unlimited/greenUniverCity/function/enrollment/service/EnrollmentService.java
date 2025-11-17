package com.univercity.unlimited.greenUniverCity.function.enrollment.service;

import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentDTO;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentDTO> findAllEnrollment();

    int addEnrollment(EnrollmentDTO enrollmentDTO);
}
