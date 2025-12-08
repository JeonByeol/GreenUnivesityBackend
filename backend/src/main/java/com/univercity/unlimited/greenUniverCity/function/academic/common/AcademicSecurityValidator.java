package com.univercity.unlimited.greenUniverCity.function.academic.common;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.community.review.exception.DataIntegrityException;
import com.univercity.unlimited.greenUniverCity.function.community.review.exception.InvalidRoleException;
import com.univercity.unlimited.greenUniverCity.function.community.review.exception.UnauthorizedException;
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
}
