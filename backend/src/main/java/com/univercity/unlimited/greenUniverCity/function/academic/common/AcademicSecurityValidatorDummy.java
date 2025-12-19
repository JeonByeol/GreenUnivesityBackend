package com.univercity.unlimited.greenUniverCity.function.academic.common;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.review.entity.Review;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.DataIntegrityException;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.InvalidRoleException;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.UnauthorizedException;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.util.exception.BusinessLogicException;
import com.univercity.unlimited.greenUniverCity.util.exception.DuplicateResourceException;
import com.univercity.unlimited.greenUniverCity.util.exception.EntityNotFoundException; // 필요 시 프로젝트 내 Exception 경로로 수정
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AcademicSecurityValidatorDummy {

    private final UserService userService;

    // ==================================================================================
    // 1.공통 유틸리티 메서드
    // ==================================================================================

    /**
     * V-1) [공통] 엔티티 조회 및 예외 처리 (Null Safety)
     * - ServiceImpl 코드 단축용
     */
    public <T, ID> T getEntityOrThrow(JpaRepository<T, ID> repository, ID id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityName + " 정보를 찾을 수 없습니다. (ID: " + id + ")"));
    }

    /**
     * V-2) [검증] 중복 데이터 존재 여부 확인
     */

    // 상황 A: 간단하게 표준 문구 사용 (2번 생성자 활용)
    public void validateDuplicate(boolean exists, String targetName) {
        if (exists) {
            // 이미 존재하는 [성적] 입니다. 라고 자동 완성됨
            throw new DuplicateResourceException(targetName, true);
        }
    }

    // 상황 B: (만약 필요하다면) 구체적인 메시지 사용 (1번 생성자 활용)
    public void validateDuplicateCustomMessage(boolean exists, String customMessage) {
        if (exists) {
            // 내가 입력한 메시지 그대로 나감
            throw new DuplicateResourceException(customMessage);
        }
    }

    /**
     * V-3) [검증] 데이터 존재 여부 확인 (있어야만 통과)
     */
    public void validateNotEmpty(boolean isEmpty, String errorMessage) {
        if (isEmpty) {
            throw new BusinessLogicException(errorMessage);
        }
    }

    /**
     * V-4) [보안] 본인 확인 (단순 이메일 비교 - DB 조회 없이 빠름)
     */
    public void validateOwner(String ownerEmail, String requesterEmail, String errorMessage) {
        if (!ownerEmail.equals(requesterEmail)) {
            log.warn("권한 위반: 요청자[{}] != 소유자[{}]", requesterEmail, ownerEmail);
            throw new UnauthorizedException(errorMessage);
        }
    }

    // ==================================================================================
    // 2. 교수 권한 검증
    // ==================================================================================

    /**
     * AC-security) 보안검사: CourseOffering 기준
     * - 클로드 피드백 반영: AC-security-3 검증 복원 및 상세 로그 유지
     */
    public void validateProfessorOwnership(CourseOffering offering, String requesterEmail, String action) {
        log.info("4) {} - 교수 권한 검증 시작: {}", action, requesterEmail);

        // 1. 요청자 조회
        User requester = userService.getUserByEmail(requesterEmail);

        // 2. 관리자는 프리패스
        if (requester.getUserRole() == UserRole.ADMIN) {
            log.info("4) 관리자 권한 확인됨 - {} 허용: {}", action, requesterEmail);
            return;
        }

        // 3. 요청자가 교수가 맞는지 확인
        if (requester.getUserRole() != UserRole.PROFESSOR) {
            throw new InvalidRoleException(
                    String.format("AC-security-1 (%s) 교수 권한이 없습니다. 요청자: %s, 요청자 권한: %s",
                            action, requesterEmail, requester.getUserRole()));
        }

        // 4. 담당 교수 존재 여부 확인
        User professor = offering.getProfessor();
        if (professor == null) {
            throw new DataIntegrityException(
                    String.format("AC-security-2 (%s) 데이터 오류: 개설 강의에 담당 교수가 없습니다. offeringId: %s",
                            action, offering.getOfferingId()));
        }

        // 5. [중요 복원] 담당자로 지정된 사람이 진짜 교수 권한을 가졌는지 확인 (무결성 체크)
        if (professor.getUserRole() != UserRole.PROFESSOR && professor.getUserRole() != UserRole.ADMIN) {
            throw new InvalidRoleException(
                    String.format("AC-security-3 (%s) 데이터 오류: 담당자가 교수 권한이 없습니다. userId: %s, Role: %s",
                            action, professor.getUserId(), professor.getUserRole()));
        }

        // 6. 요청자와 담당 교수 일치 확인
        if (!professor.getUserId().equals(requester.getUserId())) {
            throw new UnauthorizedException(
                    String.format("AC-security-4 (%s) 타인의 강의입니다. 담당: %s, 요청자: %s",
                            action, professor.getEmail(), requesterEmail));
        }

        log.info("4) 교수 권한 검증 완료 - 교수: {}, 작업: {}", requesterEmail, action);
    }

    /**
     * AC-security) ClassSection 기준 (오버로딩)
     */
    public void validateProfessorOwnership(ClassSection section, String requesterEmail, String action) {
        CourseOffering offering = section.getCourseOffering();
        if (offering == null) {
            throw new DataIntegrityException(
                    String.format("AC-security-5 (%s) 데이터 오류: 분반에 개설 강의 정보가 없습니다. sectionId: %s",
                            action, section.getSectionId()));
        }
        validateProfessorOwnership(offering, requesterEmail, action);
    }

    /**
     * AC-security) Enrollment 기준 (오버로딩)
     */
    public void validateProfessorOwnership(Enrollment enrollment, String requesterEmail, String action) {
        ClassSection section = enrollment.getClassSection();
        if (section == null) {
            throw new DataIntegrityException(
                    String.format("AC-security-6 (%s) 데이터 오류: 수강신청에 분반 정보가 없습니다. enrollmentId: %s",
                            action, enrollment.getEnrollmentId()));
        }
        validateProfessorOwnership(section, requesterEmail, action);
    }


    // ==================================================================================
    // 3. 학생/리뷰 권한 검증
    // ==================================================================================

    /**
     * AC-security) 학생 수강신청 소유권 확인
     */
    public void validateStudentEnrollmentOwnership(Enrollment enrollment, String requesterEmail, String action) {
        User enrollmentOwner = enrollment.getUser();

        if (enrollmentOwner == null) {
            throw new DataIntegrityException(
                    String.format("AC-security-7 (%s) 데이터 오류: 수강신청에 학생 정보가 없습니다. enrollmentId: %s",
                            action, enrollment.getEnrollmentId()));
        }

        if (!enrollmentOwner.getEmail().equals(requesterEmail)) {
            // 학생이 문제가 될 법한 리뷰(욕설,선정적,정치적,...) 등을 게시하면 관리자가 삭제하기 위해 관리자 권한 추가
            User requester = userService.getUserByEmail(requesterEmail);
            if (requester.getUserRole() == UserRole.ADMIN) return; 

            throw new UnauthorizedException(
                    String.format("AC-security-8 (%s) 본인 수강 과목만 작업 가능. 학생: %s, 요청: %s",
                            action, enrollmentOwner.getEmail(), requesterEmail));
        }
    }

    /**
     * AC-security) 리뷰 소유권 확인
     */
    public void validateReviewOwnership(Review review, String requesterEmail, String action) {
        Enrollment enrollment = review.getEnrollment();
        if (enrollment == null) {
            throw new DataIntegrityException(
                    String.format("AC-security-9 (%s) 데이터 오류: 리뷰에 수강신청 정보가 없습니다. reviewId: %s",
                            action, review.getReviewId()));
        }
        validateStudentEnrollmentOwnership(enrollment, requesterEmail, action);
    }
}