package com.univercity.unlimited.greenUniverCity.function.enrollment.service;

import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.LegacyEnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;

import java.util.List;
import java.util.Map;

public interface EnrollmentService {
    //E-1)Enroll에 존재하는 모든 데이터 조회
    List<LegacyEnrollmentDTO> legacyFindAllEnrollment();

    List<EnrollmentResponseDTO> findAllEnrollment();
    List<EnrollmentResponseDTO> findById(Long enrollmentId);
    EnrollmentResponseDTO createEnrollmentByAuthorizedUser(EnrollmentCreateDTO dto, String email);
    EnrollmentResponseDTO updateEnrollmentByAuthorizedUser(EnrollmentUpdateDTO dto, String email);
    Map<String,String> deleteByEnrollmentId(Long enrollmentId, String email);



    //E-2) **(기능 입력 바랍니다/사용 안할거면 삭제 부탁드립니다)**
    int addEnrollment(LegacyEnrollmentDTO legacyEnrollmentDTO);

    //E3)다른 service에서 enrollment와 여기에 속한 상위 테이블의 정보를 실질적으로 사용하기 위한 service
    Enrollment getEnrollmentEntity(Long id);


}
/**
 * 다른 클래스에 정보를 전달할 때 repository가 옳은지 service가 옳은지 검증 후 추후 사용 혹은 삭제 예정
 */
//EnrollmentTestDTO getEnrollmentForGrade(Long id);//E-4)다른 service에서 enrollment에 대한 정보를 받아서 단순 조회하는 service
//List<EnrollmentDTO> getEnrollmentFindUser(Long id);//E-5)다른 service에서 enrollment에 대한 정보를 받아오기 위해 만든 service
///