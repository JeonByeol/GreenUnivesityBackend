package com.univercity.unlimited.greenUniverCity.function.academic.assignment.service;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment.AssignmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment.AssignmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment.AssignmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Assignment;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.repository.AssignmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.common.EntityLoader;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.repository.ClassSectionRepository;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ClassSectionRepository classSectionRepository; // 분반 직접 조회를 위해 추가 (Validator 활용)

    private final AcademicSecurityValidator securityValidator; // 보안 검증 유틸리티 주입
    private final EntityMapper entityMapper;

    //AS-1)(공통) 특정 분반(Section)에 등록된 모든 과제 목록을 조회하는 서비스
    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponseDTO> findAssignmentsBySectionId(Long sectionId) {
        log.info("AS-1) 분반별 과제 목록 조회 요청 - sectionId: {}", sectionId);

        // 분반 존재 여부 확인 (Optional)
        getSectionOrThrow(sectionId);

        return assignmentRepository.findBySectionId(sectionId).stream()
                .map(entityMapper::toAssignmentResponseDTO)
                .toList();
    }

    //AS-2)(관리자or교수) 특정 분반에 새로운 과제를 생성(출제)하기 위해 동작하는 서비스
    @Override
    public AssignmentResponseDTO createAssignment(AssignmentCreateDTO dto, String email) {
        log.info("AS-2) 과제 생성 요청 - title: {}, 사용자: {}", dto.getTitle(), email);

        // 1. 분반 엔티티 조회 (Validator V-1 활용)
        ClassSection section = getSectionOrThrow(dto.getSectionId());

        // 2. [보안] 교수가 해당 분반(강의)의 담당자인지 검증 (Validator AC-security 활용)
        securityValidator.validateProfessorOwnership(section, email, "과제 생성");

        // 3. 과제 엔티티 생성
        Assignment assignment = Assignment.builder()
                .classSection(section)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .maxScore(dto.getMaxScore())
                .build();

        // 4. 저장 및 반환
        return entityMapper.toAssignmentResponseDTO(assignmentRepository.save(assignment));
    }

    //AS-3)(관리자or교수) 기존에 출제한 과제의 내용이나 기한을 수정하기 위한 서비스
    @Override
    public AssignmentResponseDTO updateAssignment(AssignmentUpdateDTO dto, String email) {
        log.info("AS-3) 과제 수정 요청 - assignmentId: {}, 사용자: {}", dto.getAssignmentId(), email);

        // 1. 과제 엔티티 조회
        Assignment assignment = getAssignmentOrThrow(dto.getAssignmentId());

        // 2. [보안] 해당 과제가 속한 분반의 담당 교수인지 확인 (Section을 통해 검증)
        securityValidator.validateProfessorOwnership(assignment.getClassSection(), email, "과제 수정");

        // 3. Dirty Checking으로 업데이트
        assignment.updateAssignment(
                dto.getTitle(),
                dto.getDescription(),
                dto.getDueDate(),
                dto.getMaxScore()
        );

        return entityMapper.toAssignmentResponseDTO(assignment);
    }

    //AS-4)(관리자or교수) 잘못 출제된 과제를 삭제하기 위한 서비스 (관련 제출물도 함께 삭제 고려)
    @Override
    public void deleteAssignment(Long assignmentId, String email) {
        log.info("AS-4) 과제 삭제 요청 - assignmentId: {}, 사용자: {}", assignmentId, email);

        // 1. 과제 엔티티 조회 (Validator V-1 활용)
        Assignment assignment = getAssignmentOrThrow(assignmentId);

        // 2. [보안] 담당 교수 확인
        securityValidator.validateProfessorOwnership(assignment.getClassSection(), email, "과제 삭제");

        // 3. 삭제 수행 (Cascade 설정에 따라 제출물도 함께 삭제될 수 있음)
        assignmentRepository.delete(assignment);
    }

    //AS-5)(공통) 과제 상세 내용을 확인하기 위한 단건 조회 서비스
    @Override
    @Transactional(readOnly = true)
    public AssignmentResponseDTO getAssignment(Long assignmentId) {
        // 단건 조회 및 DTO 변환
        Assignment assignment = getAssignmentOrThrow(assignmentId);

        return entityMapper.toAssignmentResponseDTO(assignment);
    }
    
    //AS-6) 전체조회 서비스
    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponseDTO> findAllAssignments() {
        log.info("AS-ALL) 관리자용 과제 전체 목록 조회 요청");

        return assignmentRepository.findAllWithFetchJoin().stream()
                .map(entityMapper::toAssignmentResponseDTO)
                .toList();
    }

    // =========================================================================
    //  함수
    // =========================================================================
    private Assignment getAssignmentOrThrow(Long id) {
        return securityValidator.getEntityOrThrow(assignmentRepository, id, "과제");
    }

    private ClassSection getSectionOrThrow(Long id) {
        return securityValidator.getEntityOrThrow(classSectionRepository, id, "분반");
    }
}
