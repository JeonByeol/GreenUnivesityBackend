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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AcademicSecurityValidator {
    private final UserService userService;

    /**
     * AC-security) 보안검사:
     * - 교수 or 관리자 권함검증
     *  사용처:
     *  T-:(T-4/T-5/T-6)
     *  SE-:(SE-3/SE-4/SE-5)
     *  G,GI,SS-:(Grade)
     */
    public void validateProfessorOwnership(CourseOffering offering, String requesterEmail, String action) {
        log.info("4) {} - 교수 권한 검증 시작: {}", action, requesterEmail);

        // 1. 요청자 조회
        User requester = userService.getUserByEmail(requesterEmail);

        // 2. 관리자는 모든 작업 허용
        if (requester.getUserRole() == UserRole.ADMIN) {
            log.info("4) 관리자 권한 확인됨 - {} 허용: {}", action, requesterEmail);
            return;
        }

        // 3. 요청자의 교수 권한 확인
        if (requester.getUserRole() != UserRole.PROFESSOR) {
            throw new InvalidRoleException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: AC-security-1 (%s) " +
                                    "교수 권한이 없습니다. " +
                                    "요청자: %s, userId: %s, 현재 역할: %s",
                            action, requesterEmail, requester.getUserId(), requester.getUserRole())
            );
        }

        // 4. 담당 교수 확인
        User professor = offering.getProfessor();

        if (professor == null) {
            throw new DataIntegrityException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: AC-security-2 (%s) " +
                                    "데이터 오류: 개설 강의에 담당 교수가 없습니다. offeringId: %s",
                            action, offering.getOfferingId())
            );
        }

        // 5. 담당 교수의 역할 확인
        if (professor.getUserRole() != UserRole.PROFESSOR
                && professor.getUserRole() != UserRole.ADMIN) {
            throw new InvalidRoleException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: AC-security-3 (%s) " +
                                    "데이터 오류: 담당자가 교수 권한이 없습니다. " +
                                    "userId: %s, 현재 역할: %s",
                            action, professor.getUserId(), professor.getUserRole())
            );
        }

        // 6. 요청자와 담당 교수 일치 확인
        if (!professor.getUserId().equals(requester.getUserId())) {
            throw new UnauthorizedException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: AC-security-4 (%s) " +
                                    "해당 강의의 담당 교수만 작업할 수 있습니다. " +
                                    "담당 교수: %s (userId: %s), 요청자: %s (userId: %s)",
                            action,
                            professor.getEmail(), professor.getUserId(),
                            requesterEmail, requester.getUserId())
            );
        }

        log.info("4) 교수 권한 검증 완료 - 교수: {}, 작업: {}", requesterEmail, action);
    }


    /**
     * AC-security) 보안검사 - ClassSection 기준 (오버로딩)
     * ClassSection에서 CourseOffering을 추출하여 기존 검증 로직 재사용
     */
    public void validateProfessorOwnership(ClassSection section, String requesterEmail, String action) {
        log.info("4) {} - ClassSection 기준 교수 권한 검증: sectionId={}", action, section.getSectionId());

        CourseOffering offering = section.getCourseOffering();

        if (offering == null) {
            throw new DataIntegrityException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: AC-security-5 (%s) " +
                                    "데이터 오류: 분반에 개설 강의 정보가 없습니다. sectionId: %s",
                            action, section.getSectionId())
            );
        }

        // 기존 검증 로직 재사용
        validateProfessorOwnership(offering, requesterEmail, action);
    }


    /**
     * AC-security) 보안검사 - Enrollment 기준 (오버로딩)
     * Enrollment에서 ClassSection → CourseOffering을 추출하여 검증
     */
    public void validateProfessorOwnership(Enrollment enrollment, String requesterEmail, String action) {
        log.info("4) {} - Enrollment 기준 교수 권한 검증: enrollmentId={}", action, enrollment.getEnrollmentId());

        ClassSection section = enrollment.getClassSection();

        if (section == null) {
            throw new DataIntegrityException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: AC-security-6 (%s) " +
                                    "데이터 오류: 수강신청에 분반 정보가 없습니다. enrollmentId: %s",
                            action, enrollment.getEnrollmentId())
            );
        }

        // ClassSection 검증 로직 재사용 (내부적으로 CourseOffering 검증으로 이어짐)
        validateProfessorOwnership(section, requesterEmail, action);
    }

    /*
    ===================================== Review =====================================
     */

    /**
     * AC-security) 보안검사 - 학생 수강신청 소유권 확인
     * 사용처: R-:(R-3-1) 리뷰 작성 시
     */
    public void validateStudentEnrollmentOwnership(Enrollment enrollment, String requesterEmail, String action) {
        log.info("4) {} - 학생 수강신청 소유권 검증: enrollmentId={}", action, enrollment.getEnrollmentId());

        User enrollmentOwner = enrollment.getUser();

        if (enrollmentOwner == null) {
            throw new DataIntegrityException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: AC-security-7 (%s) " +
                                    "데이터 오류: 수강신청에 학생 정보가 없습니다. enrollmentId: %s",
                            action, enrollment.getEnrollmentId())
            );
        }

        if (!enrollmentOwner.getEmail().equals(requesterEmail)) {
            throw new UnauthorizedException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: AC-security-8 (%s) " +
                                    "본인이 수강한 과목에만 작업할 수 있습니다. " +
                                    "수강신청 학생: %s (userId: %s), 요청자: %s",
                            action,
                            enrollmentOwner.getEmail(), enrollmentOwner.getUserId(),
                            requesterEmail)
            );
        }

        log.info("4) 학생 수강신청 소유권 검증 완료 - 학생: {}, 작업: {}", requesterEmail, action);
    }

    /**
     * AC-security) 보안검사 - 리뷰 소유권 확인
     * 사용처: R-:(R-4-1) 리뷰 수정/삭제 시
     */
    public void validateReviewOwnership(Review review, String requesterEmail, String action) {
        log.info("4) {} - 리뷰 소유권 검증: reviewId={}", action, review.getReviewId());

        Enrollment enrollment = review.getEnrollment();

        if (enrollment == null) {
            throw new DataIntegrityException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: AC-security-9 (%s) " +
                                    "데이터 오류: 리뷰에 수강신청 정보가 없습니다. reviewId: %s",
                            action, review.getReviewId())
            );
        }

        // Enrollment을 통해 학생 소유권 검증 (기존 로직 재사용)
        validateStudentEnrollmentOwnership(enrollment, requesterEmail, action);

        log.info("4) 리뷰 소유권 검증 완료 - 학생: {}, reviewId: {}", requesterEmail, review.getReviewId());
    }
}


