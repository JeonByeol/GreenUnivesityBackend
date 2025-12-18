package com.univercity.unlimited.greenUniverCity.function.academic.timetable.exception;

/**
 * 강의실 시간 충돌 예외
 * - 같은 강의실에 같은 시간대에 여러 분반/시간표가 배정될 때 발생
 */
public class ClassroomConflictException extends RuntimeException {
    public ClassroomConflictException(String message) {
        super(message);
    }
}
