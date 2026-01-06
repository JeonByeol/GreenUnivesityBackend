package com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.service;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.entity.AcademicTerm;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.respository.AcademicTermRepository;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import com.univercity.unlimited.greenUniverCity.util.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AcademicTermServiceImpl implements AcademicTermService {
    // Repository
    private final AcademicTermRepository repository;

    // Entity -> ResponseDTO
    private final EntityMapper entityMapper;
    // CreateDTO, UpdateDTO -> Entity
    private final AcademicTermApplyMapper applyMapper;
    // Validate ResponseDTO
    private final AcademicTermValidationService validationService;

    // Common Name
    private final String baseName = "AcademicTerm";

    // ===============================
    // 단건 조회 헬퍼 (검증 제외, 변환 전용)
    // ===============================
    private AcademicTerm findOneEntityById(Long id) {
        log.info("1) {} 단건 조회 시작: id={}", baseName, id);
        AcademicTerm entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(baseName + " not found with id: " + id));
        applyMapper.validateEntity(entity);
        log.info("2) {} 단건 조회 완료: {}", baseName, entity.getTermId());
        return entity;
    }

    // ===============================
    // 전체 조회
    // ===============================
    @Override
    public List<AcademicTermResponseDTO> findAllTerm() {
        log.info("{} 전체 조회 시작", baseName);

        List<AcademicTerm> entityList = repository.findAll();
        List<AcademicTermResponseDTO> responseDTOList = new ArrayList<>();

        for (AcademicTerm entity : entityList) {
            try {
                AcademicTermResponseDTO responseDTO = entityMapper.toAcademicTermResponseDTO(entity);
                validationService.validateResponse(responseDTO);
                responseDTOList.add(responseDTO);
            } catch (Exception e) {
                log.error("{} 변환 실패: termId={}, semester={}, error={}",
                        baseName, entity.getTermId(), entity.getSemester(), e.getMessage(), e);
            }
        }

        log.info("{} 전체 조회 완료: 성공 {}건 / 전체 {}건",
                baseName, responseDTOList.size(), entityList.size());

        return responseDTOList;
    }

    // ===============================
    // 단건 조회
    // ===============================
    @Override
    public AcademicTermResponseDTO findById(Long termId) {
        AcademicTerm entity = findOneEntityById(termId);

        // 변환 + 검증
        AcademicTermResponseDTO responseDTO = entityMapper.toAcademicTermResponseDTO(entity);
        validationService.validateResponse(responseDTO);

        log.info("{} 조회 완료: termId={}", baseName, termId);
        return responseDTO;
    }

    // ===============================
    // 생성
    // ===============================
    @Override
    @Transactional
    public AcademicTermResponseDTO createTerm(AcademicTermCreateDTO dto) {
        log.info("{} 생성 시작: {}", baseName, dto);

        // DTO -> Entity (검증 포함)
        AcademicTerm entity = applyMapper.applyCreate(dto);

        AcademicTerm savedEntity = repository.save(entity);
        log.info("{} 생성 완료: termId={}", baseName, savedEntity.getTermId());

        // Entity -> ResponseDTO + 검증
        AcademicTermResponseDTO responseDTO = entityMapper.toAcademicTermResponseDTO(savedEntity);
        validationService.validateResponse(responseDTO);

        return responseDTO;
    }

    // ===============================
    // 수정
    // ===============================
    @Override
    @Transactional
    public AcademicTermResponseDTO updateTerm(AcademicTermUpdateDTO dto) {
        log.info("{} 수정 시작: termId={}", baseName, dto.getTermId());

        // 단건 조회
        AcademicTerm entity = findOneEntityById(dto.getTermId());

        // DTO -> Entity 업데이트 (검증 포함)
        AcademicTerm updatedEntity = applyMapper.applyUpdate(dto, entity);

        AcademicTerm savedEntity = repository.save(updatedEntity);
        log.info("{} 수정 완료: termId={}", baseName, savedEntity.getTermId());

        // Entity -> ResponseDTO + 검증
        AcademicTermResponseDTO responseDTO = entityMapper.toAcademicTermResponseDTO(savedEntity);
        validationService.validateResponse(responseDTO);

        return responseDTO;
    }

    // ===============================
    // 삭제
    // ===============================
    @Override
    @Transactional
    public Map<String, String> deleteTerm(Long termId) {
        log.info("{} 삭제 시작: termId={}", baseName, termId);

        AcademicTerm entity = findOneEntityById(termId);
        repository.delete(entity);

        log.info("{} 삭제 완료: termId={}", baseName, termId);
        return Map.of("Result", "Success");
    }
}
