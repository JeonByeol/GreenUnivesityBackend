package com.univercity.unlimited.greenUniverCity.config;

import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.DuplicateReviewException;
import com.univercity.unlimited.greenUniverCity.util.exception.BusinessLogicException;
import com.univercity.unlimited.greenUniverCity.util.exception.DuplicateResourceException;
import com.univercity.unlimited.greenUniverCity.util.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================================================================================
    // 1. 비즈니스 로직 / 유효성 검증 예외 (4xx Client Error)
    // ==================================================================================

    /**
     * [400 Bad Request] 비즈니스 로직 위반
     * - 예: 빈 값 입력, 기간 위반, 잘못된 상태 변경 등
     */
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<String> handleBusinessLogicException(BusinessLogicException e) {
        log.warn("비즈니스 로직 오류: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * [409 Conflict] 리소스 중복
     * - 예: 이미 등록된 수강신청, 이미 작성한 성적 등
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateResourceException(DuplicateResourceException e) {
        log.warn("중복 데이터 오류: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    /**
     * [409 Conflict] (구버전 호환용) 리뷰 중복
     * - 점차 DuplicateResourceException으로 대체하고 이 핸들러는 삭제하세요.
     */
    @ExceptionHandler(DuplicateReviewException.class)
    public ResponseEntity<String> handleDuplicateReviewException(DuplicateReviewException e) {
        log.warn("중복 리뷰 예외 (Legacy): {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    /**
     * [404 Not Found] 데이터 없음
     * - getEntityOrThrow()에서 사용하는 예외
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("데이터 찾기 실패: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // ==================================================================================
    // 2. 서버 내부 오류 (5xx Server Error)
    // ==================================================================================

    /**
     * [500 Internal Server Error] 최후의 보루
     * - 위에서 잡지 못한 모든 에러(NullPointer, DB Connection Fail 등)는 여기서 잡습니다.
     * - 클라이언트에게는 구체적인 에러 내용을 숨기고 "서버 오류"라고만 알려주는 것이 보안상 좋습니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("알 수 없는 서버 에러 발생: ", e); // 스택 트레이스 전체 로깅
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.");
    }
}
