package com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.service;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermUpdateDTO;

import java.util.List;
import java.util.Map;

public interface AcademicTermService {
    // 조회
    List<AcademicTermResponseDTO> findAllTerm();
    AcademicTermResponseDTO findById(Long termId);

    // 쓰기
    AcademicTermResponseDTO createTerm(AcademicTermCreateDTO dto);

    // 수정
    AcademicTermResponseDTO updateTerm(AcademicTermUpdateDTO dto);

    // 삭제
    Map<String, String> deleteTerm(Long termId);

}
