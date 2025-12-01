package com.univercity.unlimited.greenUniverCity.config;

import com.univercity.unlimited.greenUniverCity.function.community.review.exception.DuplicateReviewException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 예외처리로 인한 오류 발생시 프론트앤드에 어떤 오류가 발생했는지 알려주기 위해 만든 Exception 핸들러
     */

    /**
     * [커스텀 예외 처리]
     * ReviewService에서 "이미 리뷰를 작성했습니다"라고 던지면 여기서 잡습니다.
     */
    @ExceptionHandler(DuplicateReviewException.class)
    public ResponseEntity<String> handleDuplicateReviewException(DuplicateReviewException e) {
        log.warn("중복 리뷰 예외 감지: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * [일반 Exception 처리 추가] - 이게 핵심!
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("예외 발생: {}", e.getMessage(), e);

        // DuplicateReviewException이 여기로 넘어온 경우
        if (e.getCause() instanceof DuplicateReviewException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getCause().getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + e.getMessage());
    }

    /**
     * [기타 예외 처리]
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        log.error("런타임 에러: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
