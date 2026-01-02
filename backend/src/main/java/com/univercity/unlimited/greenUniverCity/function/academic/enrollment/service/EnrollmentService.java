package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserResponseDTO;

import java.util.List;
import java.util.Map;

public interface EnrollmentService {
    // -- Enrollment -- (E)
    //E-1)Enroll에 존재하는 모든 데이터 조회
    List<EnrollmentResponseDTO> legacyFindAllEnrollment();

    List<EnrollmentResponseDTO> findAllEnrollment();
    List<EnrollmentResponseDTO> findById(Long enrollmentId);
    EnrollmentResponseDTO createEnrollmentByAuthorizedUser(EnrollmentCreateDTO dto, String email);
    EnrollmentResponseDTO updateEnrollmentByAuthorizedUser(EnrollmentUpdateDTO dto, String email);
    Map<String,String> deleteByEnrollmentId(Long enrollmentId, String email);

    Map<CourseOfferingResponseDTO,List<UserResponseDTO>> findAllStudentsByProfessorEmail(String professorEmail);
    List<EnrollmentResponseDTO> findMyEnrollments(String email);

    // -- 전체 Entity --
    //E.All)다른 service에서 enrollment와 여기에 속한 상위 테이블의 정보를 실질적으로 사용하기 위한 service
    Enrollment getEnrollmentEntity(Long id);



}
