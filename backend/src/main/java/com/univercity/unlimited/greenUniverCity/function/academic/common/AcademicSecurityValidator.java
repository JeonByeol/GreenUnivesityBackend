package com.univercity.unlimited.greenUniverCity.function.academic.common;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Submission;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.StudentScore;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.review.entity.Review;
import com.univercity.unlimited.greenUniverCity.util.exception.DataIntegrityException;
import com.univercity.unlimited.greenUniverCity.util.exception.InvalidRoleException;
import com.univercity.unlimited.greenUniverCity.util.exception.UnauthorizedException;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.util.exception.BusinessLogicException;
import com.univercity.unlimited.greenUniverCity.util.exception.DuplicateResourceException;
import com.univercity.unlimited.greenUniverCity.util.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AcademicSecurityValidator {
    private final UserService userService;

    // ==================================================================================
    // 1. 공통 유틸리티 메서드
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
    // 2. 신분(Role) 검증 - (Code: ROLE)
    // ==================================================================================

    /**
     * [관리자] 관리자 신분인지 확인
     */
    public void validateAdminRole(String requesterEmail, String action) {
        log.info("AC-SEC) 관리자 신분 검증 시작 - 작업: {}, 요청자: {}", action, requesterEmail);

        User requester = userService.getUserByEmail(requesterEmail);

        if (requester.getUserRole() != UserRole.ADMIN) {
            log.warn("AC-SEC-Warning) 관리자 권한 위반 - 작업: {}, 요청자: {}", action, requesterEmail);
            throw new InvalidRoleException(
                    String.format("AC-SEC-ROLE-01 (%s) 관리자 권한이 필요합니다. 요청자: %s, Role: %s (접근 불가)",
                            action, requesterEmail, requester.getUserRole())
            );
        }
        log.info("AC-SEC) 관리자 신분 검증 완료 (통과)");
    }


    /**
     * [교수/관리자] 교수 또는 관리자 신분인지 확인 (범용 체크)
     */
    public void validateProfessorOrAdminRole(String requesterEmail, String action) {
        log.info("AC-SEC) 교수/관리자 신분 검증 시작 - 작업: {}, 요청자: {}", action, requesterEmail);

        User requester = userService.getUserByEmail(requesterEmail);
        UserRole role = requester.getUserRole();

        if (role != UserRole.PROFESSOR && role != UserRole.ADMIN) {
            log.warn("AC-SEC-Warning) 교수/관리자 권한 위반 - 작업: {}, 요청자: {}", action, requesterEmail);
            throw new InvalidRoleException(
                    String.format("AC-SEC-ROLE-02 (%s) 교수 또는 관리자 권한이 필요합니다. 요청자: %s, Role: %s",
                            action, requesterEmail, role)
            );
        }
    }

    /**
     * [학생] 학생 신분인지 확인
     */
    public void validateStudentRole(String requesterEmail, String action) {
        User requester = userService.getUserByEmail(requesterEmail);

        if (requester.getUserRole() != UserRole.STUDENT) {
            log.warn("AC-SEC-Warning) 학생 권한 위반 - 작업: {}, 요청자: {}", action, requesterEmail);
            throw new InvalidRoleException(
                    String.format("AC-SEC-ROLE-03 (%s) 학생 권한이 필요합니다. 요청자: %s", action, requesterEmail)
            );
        }
    }

    // ==================================================================================
    // 3. 교수 소유권 - (Code: PROF)
    // ==================================================================================


    /**
     * [CourseOffering] 강의 담당 교수 본인 확인
     */
    public void validateProfessorOwnership(CourseOffering offering, String requesterEmail, String action) {
        log.info("AC-SEC) {} - 교수 강의 소유권 검증 시작: {}", action, requesterEmail);

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
                    String.format("AC-SEC-PROF-01 (%s) 교수 권한이 없습니다. 요청자: %s, 요청자 권한: %s",
                            action, requesterEmail, requester.getUserRole()));
        }

        // 4. 담당 교수 존재 여부 확인
        User professor = offering.getProfessor();
        if (professor == null) {
            throw new DataIntegrityException(
                    String.format("AC-SEC-PROF-02 (%s) 데이터 오류: 개설 강의에 담당 교수가 없습니다. offeringId: %s",
                            action, offering.getOfferingId()));
        }

        // 5. 담당자로 지정된 사람이 진짜 교수 권한을 가졌는지 확인 (무결성 체크)
        // 만약 관리자가 실수로 '학생'을 담당자로 등록했을 경우를 대비
        if (professor.getUserRole() != UserRole.PROFESSOR && professor.getUserRole() != UserRole.ADMIN) {
            log.error("AC-SEC-Critical) 데이터 무결성 오류 - 담당자가 교수가 아님. ID: {}, Role: {}",
                    professor.getUserId(), professor.getUserRole());
            throw new InvalidRoleException(
                    String.format("AC-SEC-PROF-03 (%s) 데이터 오류: 담당자가 교수 권한이 없습니다. userId: %s",
                            action, professor.getUserId()));
        }

        // 6. 소유권 확인 (요청자와 담당 교수 일치 확인)
        if (!professor.getUserId().equals(requester.getUserId())) {
            log.warn("AC-SEC-Warning) 타인 강의 접근 - 담당: {}, 요청: {}", professor.getEmail(), requesterEmail);
            throw new UnauthorizedException(
                    String.format("AC-SEC-PROF-04 (%s) 타인의 강의입니다. 담당: %s, 요청자: %s", action, professor.getEmail(), requesterEmail));
        }

        log.info("AC-SEC) 교수 소유권 검증 완료 - 통과");
    }

    /**
     * AC-security) ClassSection 기준 (오버로딩)
     */
    public void validateProfessorOwnership(ClassSection section, String requesterEmail, String action) {
        CourseOffering offering = section.getCourseOffering();
        if (offering == null) {
            throw new DataIntegrityException(
                    String.format("AC-SEC-PROF-05 (%s) 데이터 오류: 분반에 개설 강의 정보가 없습니다. sectionId: %s",
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
                    String.format("AC-SEC-PROF-06 (%s) 데이터 오류: 수강신청에 분반 정보가 없습니다. enrollmentId: %s",
                            action, enrollment.getEnrollmentId()));
        }
        validateProfessorOwnership(section, requesterEmail, action);
    }

    // ==================================================================================
    // 4. 학생 소유권 검증 - (Code: STUD / SUB)
    // ==================================================================================

    /**
     * [Enrollment] 수강생 본인 확인
     */
    public void validateStudentEnrollmentOwnership(Enrollment enrollment, String requesterEmail, String action) {
        User student = enrollment.getUser();

        if (student == null) {
            throw new DataIntegrityException("AC-SEC-STUD-01 데이터 오류: 수강신청에 학생 정보가 없습니다.");
        }

        User requester = userService.getUserByEmail(requesterEmail);

        // 관리자는 프리패스  (학생이 문제가 될 법한 리뷰(욕설,선정적,정치적,...) 등을 게시하면 관리자가 삭제하기 위해 관리자 권한 추가)
        if (requester.getUserRole() == UserRole.ADMIN) return;

        if (!student.getEmail().equals(requesterEmail)) {
            log.warn("AC-SEC-Warning) 타인 수강내역 접근 - 학생: {}, 요청: {}", student.getEmail(), requesterEmail);
            throw new UnauthorizedException(
                    String.format("AC-SEC-STUD-02 (%s) 본인의 수강 정보만 접근 가능합니다. 학생: %s, 요청: %s",
                            action, student.getEmail(), requesterEmail));
        }
    }

    /**
     * [Submission] 과제 제출물 소유권 확인
     */
    public void validateSubmissionOwnership(Submission submission, String requesterEmail, String action) {
        User student = submission.getStudent();
        if (student == null) throw new DataIntegrityException("AC-SEC-SUB-01 데이터 오류: 제출자 정보가 없습니다.");

        if (!student.getEmail().equals(requesterEmail)) {
            log.warn("AC-SEC-Warning) 타인 과제 접근 - 학생: {}, 요청: {}", student.getEmail(), requesterEmail);
            throw new UnauthorizedException(
                    String.format("AC-SEC-SUB-02 (%s) 본인의 과제만 수정/삭제 가능합니다.", action));
        }
    }

    /**
     * [StudentScore] 성적 소유권 확인
     */
    public void validateScoreOwnership(StudentScore score, String requesterEmail, String action) {
        if (score.getEnrollment() == null) throw new DataIntegrityException("AC-SEC-STUD-03 - 데이터 오류: 수강 정보가 없습니다.");
        validateStudentEnrollmentOwnership(score.getEnrollment(), requesterEmail, action);
    }

    /**
     * [Review] 리뷰 소유권 확인
     */
    public void validateReviewOwnership(Review review, String requesterEmail, String action) {
        if (review.getEnrollment() == null) throw new DataIntegrityException("AC-SEC-STUD-04 - 데이터 오류: 수강 정보가 없어서 리뷰를 접근 할 수 없습니다.");
        validateStudentEnrollmentOwnership(review.getEnrollment(), requesterEmail, action);
    }

    /**
     * Attendance(출결) 소유권 검증
     * - 역할: 남의 출결 정보를 조회하거나 이의 신청하는 것을 방지
     */
    public void validateAttendanceOwnership(Attendance attendance, String requesterEmail, String action) {
        if (attendance.getEnrollment() == null) {
            throw new DataIntegrityException("AC-SEC-STUD-05 - 출결 데이터 오류: 수강 정보 없음");
        }
        // 출결은 '수강신청'에 종속되므로, 수강생 본인인지 확인하는 로직 재사용
        validateStudentEnrollmentOwnership(attendance.getEnrollment(), requesterEmail, action);
    }

    // ==================================================================================
    // 5. 비즈니스 로직 검증 - (기간, 상태 등) (Code: BIZ)
    // ==================================================================================

    /**
     * 성적 입력/수정이 가능한 학기인지 확인
     * - 역할: 지난 학기 성적을 실수로 수정하는 것을 방지
     * - 예외: 관리자(ADMIN)는 수정 가능
     */
//    public void validateTermProcessingAllowed(CourseOffering offering, String requesterEmail, String action) {
//        AcademicTerm term = offering.getAcademicTerm();
//
//        // 학기 정보가 없거나, 현재 학기가 아닌 경우
//        if (term != null && !term.getIsCurrent()) {
//
//            // 관리자 권한 확인 (관리자는 지난 학기 수정 가능)
//            User requester = userService.getUserByEmail(requesterEmail);
//            if (requester.getUserRole() == UserRole.ADMIN) {
//                log.info("AC-SEC) 지난 학기 데이터 수정 허용 (관리자 권한) - 요청자: {}", requesterEmail);
//                return;
//            }
//
//            log.warn("AC-SEC-Warning) 지난 학기 데이터 접근 차단 - 요청자: {}, 학기: {}", requesterEmail, term.getTermId());
//            throw new BusinessLogicException(
//                    String.format("AC-SEC-BIZ-01 - [%s] 현재 진행 중인 학기의 데이터만 수정할 수 있습니다.", action));
//        }
//    }

}


