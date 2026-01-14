package com.univercity.unlimited.greenUniverCity.function.academic.course.service;

import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CourseValidationService {

    @Autowired
    private CourseRepository courseRepository;

    private final String baseName = "Course";

    /**
     * ResponseDTO 검증
     * - 클라이언트 반환 직전/서비스 반환 직전에 호출
     */
    public void validateResponse(CourseResponseDTO dto) {
        log.debug("{} ResponseDTO 검증 시작", baseName);

        if (dto == null) {
            throw new IllegalStateException(baseName + " ResponseDTO 는 null 일 수 없습니다.");
        }

        if (dto.getCourseId() == null) {
            log.error("{} ResponseDTO 검증 실패 - courseId 없음: {}", baseName, dto);
            throw new IllegalStateException(baseName + " id 는 비어 있을 수 없습니다.");
        }

        if (!courseRepository.existsById(dto.getCourseId())) {
            log.error("{} ResponseDTO 검증 실패 - courseId 존재하지 않음: {}", baseName, dto);
            throw new IllegalStateException(baseName + " 가 존재하지 않습니다.");
        }

        if (dto.getCourseName() == null || dto.getCourseName().isBlank()) {
            log.error("{} ResponseDTO 검증 실패 - courseName 없음: {}", baseName, dto);
            throw new IllegalStateException(baseName + " 과목명은 비어 있을 수 없습니다.");
        }

        if (dto.getCredits() == null) {
            log.error("{} ResponseDTO 검증 실패 - credits 없음: {}", baseName, dto);
            throw new IllegalStateException(baseName + " 학점은 비어 있을 수 없습니다.");
        }

        log.debug("{} ResponseDTO 검증 완료: {}", baseName, dto);
    }
}
