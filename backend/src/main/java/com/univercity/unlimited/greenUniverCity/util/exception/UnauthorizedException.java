package com.univercity.unlimited.greenUniverCity.util.exception;

/**
 * 권한이 없는 사용자가 접근할 때 발생하는 예외 (403 Forbidden 성격)
 * 예: 남의 글 수정 시도, 교수 권한 필요한데 학생이 접근 등
 */

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
