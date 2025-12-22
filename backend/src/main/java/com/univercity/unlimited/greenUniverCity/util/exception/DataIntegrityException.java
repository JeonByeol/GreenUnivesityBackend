package com.univercity.unlimited.greenUniverCity.util.exception;

/**
 * 데이터 무결성 위반 시 발생하는 예외
 * 예: 중복 데이터, 외래키 제약조건 위반 등 비즈니스 로직 상의 데이터 충돌
 */

public class DataIntegrityException extends RuntimeException{
    public DataIntegrityException(String message) {
        super(message);
    }
}
