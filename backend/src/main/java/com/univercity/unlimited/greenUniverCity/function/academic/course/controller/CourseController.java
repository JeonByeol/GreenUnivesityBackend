package com.univercity.unlimited.greenUniverCity.function.academic.course.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.service.CourseService;
import com.univercity.unlimited.greenUniverCity.function.member.department.service.DepartmentService;
import com.univercity.unlimited.greenUniverCity.util.exception.InvalidDateFormatException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;
    private final AcademicSecurityValidator securityValidator;

    private final String baseName = "CourseController";

    /// ========================================
    ///         C-1) 전체 조회
    ///         누구나 가능 (권한 체크 없음)
    /// ========================================
    @GetMapping("/all")
    public List<CourseResponseDTO> getCourseResponseDTOList(
            @RequestHeader(value = "X-User-Email", required = false) String requesterEmail
    ) {
        log.info("{} 전체 조회 시도 - 요청자: {}", baseName, requesterEmail);
        // 조회는 누구나 가능 (권한 체크 없음)
        return courseService.findAllCourse();
    }

    /// ========================================
    ///         C-2) 단 건 조회
    ///         누구나 가능 (권한 체크 없음)
    /// ========================================
    @GetMapping("/one/{courseId}")
    public CourseResponseDTO getCourseResponseDTOByCourseId(
            @PathVariable("courseId") Long courseId,
            @RequestHeader(value = "X-User-Email", required = false) String requesterEmail
    )
    {
        log.info("{} 단 건 조회 시도 - termId: {}, 요청자: {}", baseName, courseId, requesterEmail);
        return courseService.findById(courseId);
    }

    /// ========================================
    ///         C-3) 단 건 생성
    ///         관리자만 가능
    /// ========================================
    @PostMapping("/create")
    public ResponseEntity<CourseResponseDTO> createCourse(
            @Valid @RequestBody CourseCreateDTO dto,
            @RequestHeader(value = "X-User-Email", required = true) String requesterEmail
            )
    {
        log.info("{} 생성 시도 - 요청자: {}", baseName, requesterEmail);
        securityValidator.validateAdminRole(requesterEmail, baseName + " 생성");

        CourseResponseDTO createResponse = courseService.createCourse(dto);
        return ResponseEntity.ok(createResponse);
    }

    /// ========================================
    ///         C-4) 단 건 수정
    ///         관리자만 가능
    /// ========================================
    @PutMapping("/update")
    public ResponseEntity<CourseResponseDTO> updateCourse(
            @Valid @RequestBody CourseUpdateDTO dto,
            @RequestHeader(value = "X-User-Email", required = true) String requesterEmail
    )
    {
        log.info("{} 수정 시도 - 요청자: {}", baseName, requesterEmail);
        securityValidator.validateAdminRole(requesterEmail, "Term 수정");

        CourseResponseDTO updateResponse = courseService.updateCourse(dto);
        return ResponseEntity.ok(updateResponse);
    }

    /// ========================================
    ///         C-5) 단 건 삭제
    ///         관리자만 가능
    /// ========================================
    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable("courseId") Long courseId,
            @RequestHeader(value = "X-User-Email", required = true) String requesterEmail
    ) {
        log.info("{} 삭제 시도 - termId: {}, 요청자: {}", baseName, courseId, requesterEmail);
        securityValidator.validateAdminRole(requesterEmail, "Term 삭제");

        Map<String, String> result = courseService.deleteCourse(courseId);
        return ResponseEntity.ok(result.get("Result"));
    }

    // 에러 핸들러
    @ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<Map<String, Object>> handleValidationException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Bad Request");

        if (ex instanceof MethodArgumentNotValidException manvEx) {
            // DTO 검증 실패
            String message = manvEx.getBindingResult().getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .findFirst()
                    .orElse("입력값이 잘못되었습니다.");
            response.put("message", message);

        } else if (ex instanceof HttpMessageNotReadableException) {
            // JSON/날짜 포맷 오류 → InvalidDateFormatException으로 감싸기
            InvalidDateFormatException idfEx = new InvalidDateFormatException(
                    "잘못된 JSON 형식 또는 날짜 형식입니다. yyyy-MM-dd 형식으로 입력해주세요."
            );
            response.put("message", idfEx.getMessage());
        } else {
            // 기타 예외
            response.put("message", ex.getMessage());
        }

        return ResponseEntity.badRequest().body(response);
    }


}
