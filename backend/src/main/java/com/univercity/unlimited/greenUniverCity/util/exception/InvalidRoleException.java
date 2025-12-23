package com.univercity.unlimited.greenUniverCity.util.exception;

/**
 * 역할(Role)이 맞지 않을 때 발생하는 예외
 * 예: "이 기능은 교수님만 사용할 수 있습니다."
 */

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String message) {
        super(message);
    }
}

