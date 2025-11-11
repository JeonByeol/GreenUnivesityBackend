package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.EnrollmentDTO;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentDTO> findAllEnrollment();

    int addEnrollment(EnrollmentDTO enrollmentDTO);
}
