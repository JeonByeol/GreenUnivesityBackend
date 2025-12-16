package com.univercity.unlimited.greenUniverCity.function.academic.offering.service;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CourseOfferingService {
    Optional<List<CourseOfferingResponseDTO>> findAllCourseOfferingDTO();

    List<CourseOfferingResponseDTO> findAllOffering();
    List<CourseOfferingResponseDTO> findById(Long offeringId);
    CourseOfferingResponseDTO createOfferingByAuthorizedUser(CourseOfferingCreateDTO dto, String email);
    CourseOfferingResponseDTO updateOfferingByAuthorizedUser(CourseOfferingUpdateDTO dto, String email);
    Map<String,String> deleteByOfferingId(Long offeringId,String email);

    int addCourseOffering(CourseOfferingResponseDTO legacyCourseOfferingDTO);

    //CO-3) 다른 service에서 CourseOffering과 여기에 속한 상위 테이블의 정보를 실질적으로 사용하기 위한 service 선언
    CourseOffering getCourseOfferingEntity(Long id);

    //CourseOfferingDTO findByCourseNameForTimeTable(String courseName);//CO-4) Timetable에 강의명을 넘겨주기 위해 구성한 service
}
