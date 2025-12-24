package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Submission;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.repository.SubmissionRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.calculator.ScoreCalculator;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.StudentScore;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.GradeItemRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.GradeRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.Grade;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.StudentScoreRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
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
public class GradeServiceImpl implements GradeService{

    //(조회 및 검증용)
    private final GradeRepository repository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseOfferingRepository offeringRepository;

    private final EntityMapper entityMapper;
    private final AcademicSecurityValidator validator;

    // 계산기 및 레포지토리 직접 주입 (성능 최적화 & 순환참조 방지)
    private final ScoreCalculator calculator;
    private final GradeItemRepository gradeItemRepository;
    private final SubmissionRepository submissionRepository;
    private final StudentScoreRepository studentScoreRepository;

    //G-1) 성적 테이블 전체 조회 (교수 OR 관리자)
    @Override
    @Transactional(readOnly = true)
    public List<GradeResponseDTO> findAllGrades(String requesterEmail) {
        log.info("2) 성적 테이블에 존재하는 전체 데이터 조회 시작");

        validator.validateProfessorOrAdminRole(requesterEmail, "전체 성적 조회");

        return repository.findAllWithDetails().stream()
                .map(entityMapper::toGradeResponseDTO)
                .toList();
    }

    //G-2) 성적 단건 조회 (학생 본인 OR 담당 교수 OR 관리자)
    @Override
    @Transactional(readOnly = true)
    public GradeResponseDTO getGrade(Long gradeId, String requesterEmail) {
        log.info("2) 성적 단건 조회 시작 - gradeId: {}, 요청자: {}", gradeId, requesterEmail);

        Grade grade = getGradeOrThrow(gradeId);
        
        // 1차 보안 본인이면 바로 리턴
        String studentEmail = grade.getEnrollment().getUser().getEmail();
        if (studentEmail.equals(requesterEmail)) {
            return entityMapper.toGradeResponseDTO(grade);
        }

        // 2차 보안 본인이 아니라면, 담당 교수(또는 관리자)인지 확인
        CourseOffering offering = grade.getEnrollment().getClassSection().getCourseOffering();
        validator.validateProfessorOwnership(offering, requesterEmail, "성적 단건 조회");

        return entityMapper.toGradeResponseDTO(grade);
    }

    // G-3) 학생의 모든 성적 조회 (학생 본인용)
    @Override
    @Transactional(readOnly = true)
    public List<GradeResponseDTO> getStudentGrades(String studentEmail, String requesterEmail) {
        log.info("2) 학생 성적 조회 시작 - 학생-:{}, 요청자-:{}",
                studentEmail, requesterEmail);

        validator.validateOwner(studentEmail, requesterEmail, "본인의 성적만 조회할 수 있습니다.");

        List<Grade> grades= repository.findByStudentEmail(studentEmail);
        log.info("3) 학생 성적 조회 완료 - email-:{}, 성적개수-:{}",
                studentEmail, grades.size());

        return grades.stream()
                .map(entityMapper::toGradeResponseDTO)
                .toList();
    }
    
    //G-4) 강의별 모든 학생에 대한 성적 조회 (교수)
    @Override
    @Transactional(readOnly = true)
    public List<GradeResponseDTO> getOfferingGrades(Long offeringId, String professorEmail) {
        log.info("2) 특정 강의에 대한 성적 조회 시작 - offeringId-:{}, 교수-:{}",
                offeringId, professorEmail);

        CourseOffering offering = getOfferingOrThrow(offeringId);
        validator.validateProfessorOwnership(offering,professorEmail,"성적 조회");
        
        List<Grade> grades=repository.findByOfferingGrade(offeringId);

        log.info("3) 특정 강의에 대한 성적 조회 완료 - offeringId-:{}, 성적개수-:{}",
                offeringId, grades.size());

        return grades.stream()
                .map(entityMapper::toGradeResponseDTO)
                .toList();
    }

    //G-6) 성적 수정 (교수)
    @Override
    public GradeResponseDTO updateGrade(Long gradeId, GradeUpdateDTO dto, String professorEmail) {
        log.info("2) 성적 수정 시작 - gradeId-:{}, 교수-:{}",
                gradeId, professorEmail);

        Grade grade = getGradeOrThrow(gradeId);
        CourseOffering offering = grade.getEnrollment().getClassSection().getCourseOffering();

        // 1차 보안 소유권 확인
        validator.validateProfessorOwnership(offering, professorEmail, "성적수정");

        // 2치 보안 지난 학기 데이터 수정 방지
        //validator.validateTermProcessingAllowed(offering, professorEmail, "성적수정");

        grade.updateGradeInfo(dto.getTotalScore(), dto.getLetterGrade());

        log.info("3) 성적 수정 완료");

        return entityMapper.toGradeResponseDTO(repository.save(grade));
    }

    //G-7) 최종 성적 자동 계산 및 저장(StudentScore를 통해 점수 조회 후 평균 계산 (교수)
    @Override
    public GradeResponseDTO calculateAndSaveGrade(Long enrollmentId, String professorEmail) {

        log.info("2) 최종 성적 자동 계산 시작 - enrollmentId-:{}, 교수-:{}",
                enrollmentId, professorEmail);

        Enrollment enrollment = getEnrollmentOrThrow(enrollmentId);
        CourseOffering offering= enrollment.getClassSection().getCourseOffering();
        
        // 1차보안 소유권 확인
        validator.validateProfessorOwnership(offering, professorEmail, "성적계산");

        // 2치 보안 지난 학기 데이터 수정 방지
        //validator.validateTermProcessingAllowed(offering, professorEmail, "성적수정");

        // 2 평가 항목 목록 조회 (중간, 기말, 과제 등)
        List<GradeItem> gradeItems = gradeItemRepository.findByOfferingId(offering.getOfferingId());
        if (gradeItems.isEmpty()) {
            throw new IllegalStateException("이 강의에 설정된 평가 항목이 없습니다.");
        }

        // 3.계산에 필요한 데이터 일괄 조회
        Long sectionId = enrollment.getClassSection().getSectionId();
        String studentEmail = enrollment.getUser().getEmail();

        // (1) 과제 제출물 다 가져오기 (JOIN FETCH로 과제 정보까지 한 번에)
        List<Submission> submissions = submissionRepository.findAllBySectionIdAndStudentEmail(sectionId, studentEmail);

        // (2) 시험/기타 점수 다 가져오기
        List<StudentScore> studentScores = studentScoreRepository.findByEnrollmentId(enrollmentId);

        // 4. 계산
        float totalScore = calculator.calculateFinalGrade(gradeItems, submissions, studentScores);


        // 5. 저장
        Grade grade = repository.findByEnrollment_enrollmentId(enrollmentId)
                .orElse(Grade.builder().enrollment(enrollment).build());

        // 등급 산출 및 값 설정 (Grade 엔티티 내부 로직 수행)
        grade.setTotalScoreAndCalculateGrade(totalScore);
        Grade savedGrade = repository.save(grade);

        log.info("성적 계산 완료 - 총점: {:.2f}, 등급: {}", totalScore, savedGrade.getLetterGrade());
        return entityMapper.toGradeResponseDTO(savedGrade);
    }

    // G-8) 강의별 전체 학생 성적 일괄 산출
    // 역할: 수강생 목록을 가져와서 loop를 돌며 위에서 만든 '개별 계산 메서드(G-7)'를 재사용합니다.
    @Override
    public void calculateGradeForOffering(Long offeringId, String professorEmail) {
        log.info("ALL) 강의별 전체 성적 산출 시작 - offeringId: {}, 교수: {}", offeringId, professorEmail);

        CourseOffering offering = getOfferingOrThrow(offeringId);

        validator.validateProfessorOwnership(offering, professorEmail, "전체 성적 산출");
        //validator.validateTermProcessingAllowed(offering, professorEmail, "전체 성적 산출");

        // 1. 해당 강의의 수강생 목록 조회
        List<Enrollment> enrollments = enrollmentRepository.findAllByOfferingId(offeringId);

        log.info("총 {}명의 수강생에 대해 성적 산출을 진행합니다.", enrollments.size());

        // 2. 반복문 돌면서 '1명 계산 로직(G-7)' 재사용
        int successCount = 0;
        for (Enrollment enrollment : enrollments) {
            try {
                // 기존에 만든 메서드 호출 (this.calculateAndSaveGrade)
                calculateAndSaveGrade(enrollment.getEnrollmentId(), professorEmail);
                successCount++;
            } catch (Exception e) {
                log.error("학생 성적 산출 실패 - enrollmentId: {}, 에러: {}", enrollment.getEnrollmentId(), e.getMessage());
                // 한 명 실패해도 나머지는 계속 진행하도록 try-catch 처리
            }
        }

        log.info("전체 성적 산출 완료 - 성공: {}/{}", successCount, enrollments.size());
    }

    // =========================================================================
    //  Private Helper Methods
    // =========================================================================
    private Grade getGradeOrThrow(Long id) {
        return validator.getEntityOrThrow(repository, id, "성적");
    }

    private CourseOffering getOfferingOrThrow(Long id) {
        return validator.getEntityOrThrow(offeringRepository, id, "강의");
    }

    private Enrollment getEnrollmentOrThrow(Long id) {
        return validator.getEntityOrThrow(enrollmentRepository, id, "수강신청");
    }


}
