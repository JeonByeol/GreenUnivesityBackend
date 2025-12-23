package com.univercity.unlimited.greenUniverCity.util.exception;

public class DuplicateResourceException extends RuntimeException {
    // 1. 메시지를 직접 다 받는 경우(구체적인 메시지가 필요할 때) 사용
    public DuplicateResourceException(String message) {
        super(message);
    }

    // 2. [권장] 대상 이름만 받아서 문장을 자동 완성하는 경우 (통일 되는 에러 메세지가 필요할 때) 사용
    public DuplicateResourceException(String targetName, boolean autoMessage) {
        super("이미 존재하는 [" + targetName + "] 입니다.");
    }
}
