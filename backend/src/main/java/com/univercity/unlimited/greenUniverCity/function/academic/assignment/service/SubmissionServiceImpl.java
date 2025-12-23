package com.univercity.unlimited.greenUniverCity.function.academic.assignment.service;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission.SubmissionCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission.SubmissionResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission.SubmissionUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Assignment;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Submission;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.repository.AssignmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.repository.SubmissionRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.service.StudentScoreService;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import com.univercity.unlimited.greenUniverCity.util.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SubmissionServiceImpl implements SubmissionService {
    // 주석-1) SubmissionController=SB

    private final SubmissionRepository submissionRepository;
    private final AssignmentService assignmentService;
    private final UserService userService;
    private final AcademicSecurityValidator securityValidator; // ✅ 보안 검증 유틸리티
    private final EntityMapper entityMapper;

    //SB-1)(학생) 특정 과제에 대해 파일을 업로드하여 과제를 제출하는 서비스
    @Override
    public SubmissionResponseDTO submitAssignment(SubmissionCreateDTO dto, String email) {
        log.info("SB-1) 과제 제출 요청 - assignmentId: {}, 학생: {}", dto.getAssignmentId(), email);

        // 1. 과제 엔티티 조회 (AssignmentService의 AS-E 메서드 활용)
        Assignment assignment = assignmentService.getAssignmentEntity(dto.getAssignmentId());

        // 마감 기한 체크 (서버 시간 기준)
        if (LocalDateTime.now().isAfter(assignment.getDueDate())) {
            // 400 Bad Request에 해당하는 예외 던지기
            throw new BusinessLogicException("과제 제출 기한이 지났습니다. (마감일: " + assignment.getDueDate() + ")");
        }

        // 2. 이미 제출했는지 확인 (중복 제출 방지 - Validator V-2 활용 가능)
        boolean alreadySubmitted = submissionRepository.findByAssignmentIdAndStudentEmail(dto.getAssignmentId(), email).isPresent();
        if (alreadySubmitted) {
            // Validator 메서드 활용 or 직접 예외 발생
            securityValidator.validateDuplicateCustomMessage(true, "이미 과제를 제출했습니다. 수정 기능을 이용해주세요.");
        }

        // 3. 학생(User) 엔티티 조회 (UserService 활용)
        User student = userService.getUserByEmail(email);

        // 4. 제출 엔티티 생성
        Submission submission = Submission.builder()
                .assignment(assignment)
                .student(student)
                .fileUrl(dto.getFileUrl())
                .submittedAt(LocalDateTime.now())
                .score(0.0f) // 초기 점수 0점
                .build();

        return entityMapper.toSubmissionResponseDTO(submissionRepository.save(submission));
    }

    //SB-2)(학생) 이미 제출한 과제 파일을 수정(재제출)하기 위한 서비스
    @Override
    public SubmissionResponseDTO updateSubmissionFile(SubmissionUpdateDTO dto, String email) {
        log.info("SB-2) 과제 재제출(파일수정) 요청 - submissionId: {}", dto.getSubmissionId());

        // 1. 제출 내역 조회
        Submission submission = getSubmissionEntity(dto.getSubmissionId());

        if (LocalDateTime.now().isAfter(submission.getAssignment().getDueDate())) {
            throw new BusinessLogicException("마감 기한이 지나 수정할 수 없습니다.");
        }

        // 2. [보안] 본인 확인 (Validator V-4 활용)
        // 제출한 학생의 이메일과 현재 요청자 이메일 비교
        securityValidator.validateOwner(submission.getStudent().getEmail(), email, "본인의 과제만 수정할 수 있습니다.");

        // 3. 파일 경로 업데이트 (Dirty Checking)
        submission.updateFile(dto.getFileUrl());

        return entityMapper.toSubmissionResponseDTO(submission);
    }

    //SB-3)(교수) 학생이 제출한 과제에 대해 점수를 부여(채점)하는 서비스
    @Override
    public SubmissionResponseDTO gradeSubmission(Long submissionId, Float score, String email) {
        log.info("SB-3) 과제 채점 요청 - submissionId: {}, 점수: {}, 교수: {}", submissionId, score, email);

        // 1. 제출 내역 조회
        Submission submission = getSubmissionEntity(submissionId);

        // 2. [보안] 해당 과제의 담당 교수가 맞는지 검증 (Validator AC-security 활용)
        // Submission -> Assignment -> ClassSection 정보를 넘겨서 검증
        securityValidator.validateProfessorOwnership(submission.getAssignment().getClassSection(), email, "과제 채점");

        // 3. 점수 검증 (만점 초과 여부)
        Float maxScore = submission.getAssignment().getMaxScore();
        if (score < 0 || score > maxScore) {
            throw new IllegalArgumentException(
                    String.format("점수는 0점 이상, 만점(%.1f점) 이하여야 합니다. 입력된 점수: %.1f", maxScore, score)
            );
        }

        // 4. 점수 부여 (Dirty Checking)
        submission.gradeSubmission(score);

        return entityMapper.toSubmissionResponseDTO(submission);
    }

    //SB-4)(교수) 특정 과제에 대한 모든 학생들의 제출 현황을 조회하는 서비스
    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponseDTO> findAllSubmissionsByAssignment(Long assignmentId) {
        log.info("SB-4) 특정 과제 제출물 전체 조회 - assignmentId: {}", assignmentId);

        // 과제 존재 여부 확인 (Optional)
        assignmentService.getAssignmentEntity(assignmentId);

        // (선택사항: 교수의 권한을 여기서도 체크할 수 있음)

        return submissionRepository.findByAssignmentId(assignmentId).stream()
                .map(entityMapper::toSubmissionResponseDTO)
                .toList();
    }

    //SB-5)(학생) 내가 제출한 과제 내역을 확인하기 위한 단건 조회 서비스
    @Override
    @Transactional(readOnly = true)
    public SubmissionResponseDTO findMySubmission(Long assignmentId, String email) {
        log.info("SB-5) 내 과제 제출 내역 조회 - assignmentId: {}, 학생: {}", assignmentId, email);

        // 제출 내역 조회 (없으면 null 반환 -> 프론트에서 미제출 처리)
        return submissionRepository.findByAssignmentIdAndStudentEmail(assignmentId, email)
                .map(entityMapper::toSubmissionResponseDTO)
                .orElse(null);
    }
    
    //SB-6) 전체조회 서비스
    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponseDTO> findAllSubmissions() {
        log.info("SB-ALL) 관리자용 제출내역 전체 목록 조회 요청");

        // 리포지토리의 기본 findAll() 사용
        return submissionRepository.findAllWithFetchJoin().stream()
                .map(entityMapper::toSubmissionResponseDTO)
                .toList();
    }

    //SB-E) 외부 Service에서 Submission의 정보를 활용하기 위한 엔티티 조회 서비스
    @Override
    @Transactional(readOnly = true)
    public Submission getSubmissionEntity(Long submissionId) {
        // Validator V-1 활용하여 엔티티 조회
        return securityValidator.getEntityOrThrow(submissionRepository, submissionId, "제출 내역");
    }
}