package com.univercity.unlimited.greenUniverCity.function.enrollment.service;

import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentTestDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentDTO> findAllEnrollment();//E1)

    int addEnrollment(EnrollmentDTO enrollmentDTO);

    EnrollmentTestDTO getEnrollmentForGrade(Long id);//E2)다른 service에서 enrollment에 대한 정보를 받아서 단순 조회하는 service

    Enrollment getEnrollmentEntity(Long id);//E3)다른 service에서 enrollment와 여기에 속한 상위 테이블의 정보를 실질적으로 사용하기 위한 service

    List<EnrollmentDTO> getEnrollmentFindUser(Long id);//다른 service에서 enrollment에 대한 정보를 받아오기 위해 만든 service

}
