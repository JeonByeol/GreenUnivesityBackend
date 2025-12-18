package com.univercity.unlimited.greenUniverCity.function.academic.timetable.service;

import com.univercity.unlimited.greenUniverCity.function.academic.timetable.repository.TimeTableRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * 시간표 검증 로직 전용 서비스
 * - 순환 참조 방지를 위해 TimeTableService에서 검증 로직을 분리함
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TimeTableValidationService {

    private final TimeTableRepository repository;

    /**
     * 강의실 시간 중복 체크 (생성 시)
     */
    public boolean validateTimeOverlap(Long classroomId, DayOfWeek day, LocalTime start, LocalTime end) {
        boolean isOverlap = repository.existsByTimeOverlap(classroomId, day, start, end);

        if (isOverlap) {
            log.warn("시간표 중복 감지! 강의실ID: {}, 요일: {}, 시간: {}~{}",
                    classroomId, day, start, end);
        }
        return isOverlap;
    }

    /**
     * 강의실 시간 중복 체크 (수정 시 - 내 ID 제외)
     */
    public boolean validateTimeOverlapExcludingId(Long classroomId, DayOfWeek day, LocalTime start, LocalTime end, Long excludeId) {
        return repository.existsByTimeOverlapExcludingId(classroomId, day, start, end, excludeId);
    }
}
