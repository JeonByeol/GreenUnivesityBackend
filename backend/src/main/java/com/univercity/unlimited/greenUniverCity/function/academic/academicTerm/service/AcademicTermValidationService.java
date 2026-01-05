package com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.service;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.respository.AcademicTermRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AcademicTermValidationService {

    @Autowired
    AcademicTermRepository academicTermRepository;

    /**
     * ResponseDTO 검증
     * - 클라이언트 반환 직전/서비스 반환 직전에 호출
     */
    public void validateResponse(AcademicTermResponseDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("AcademicTermResponseDTO 는 null 일 수 없습니다.");
        }

        if (dto.getTermId() == null) {
            log.warn("AcademicTermResponseDTO 검증 실패 - termId 없음: {}", dto);
            throw new IllegalArgumentException("termId 는 비어 있을 수 없습니다.");
        }
        
        if(!academicTermRepository.existsById(dto.getTermId())){
            log.warn("AcademicTermResponseDTO 검증 실패 - termId 없음: {}", dto);
            throw new IllegalArgumentException("term 이 비어 있을 수 없습니다.");
        }

        if (dto.getSemester() == null || dto.getSemester().isBlank()) {
            log.warn("AcademicTermResponseDTO 검증 실패 - semester 없음: {}", dto);
            throw new IllegalArgumentException("semester 는 비어 있을 수 없습니다.");
        }

        if (dto.getRegistrationStart() == null) {
            log.warn("AcademicTermResponseDTO 검증 실패 - registrationStart 없음: {}", dto);
            throw new IllegalArgumentException("registrationStart 는 비어 있을 수 없습니다.");
        }

        if (dto.getRegistrationEnd() == null) {
            log.warn("AcademicTermResponseDTO 검증 실패 - registrationEnd 없음: {}", dto);
            throw new IllegalArgumentException("registrationEnd 는 비어 있을 수 없습니다.");
        }

        log.debug("AcademicTermResponseDTO 검증 완료: {}", dto);
    }
}
