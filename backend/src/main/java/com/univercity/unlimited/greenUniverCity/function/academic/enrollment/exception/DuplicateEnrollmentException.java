package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.exception;

/**
 * 중복 수강신청 예외
 * - 같은 학생이 같은 분반에 2번 이상 신청할 때 발생
 */
public class DuplicateEnrollmentException extends RuntimeException {
    public DuplicateEnrollmentException(String message) {
        super(message);
    }
}
