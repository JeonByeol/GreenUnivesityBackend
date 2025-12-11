package com.univercity.unlimited.greenUniverCity.function.academic.grade.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.service.StudentScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/student-scores")
public class StudentScoreController {

    private final StudentScoreService scoreService;

    //SS-1) 학생 점수 생성을 위해 컨트롤러 내부에 선언된 CRUD(POST)
    @PostMapping("/create")
    public ResponseEntity<StudentScoreResponseDTO> createStudentScore(
            @Valid @RequestBody StudentScoreCreateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String professorEmail) {

        log.info("1) 학생 점수 생성 요청 - enrollmentId: {}, itemId: {}, 교수: {}",
                dto.getEnrollmentId(), dto.getItemId(), professorEmail);

        // Postman 테스트용
        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            professorEmail = "hannah@aaa.com";
        }

        StudentScoreResponseDTO response =
                scoreService.createStudentScore(dto, professorEmail);

        log.info("학생 점수 생성 완료 - scoreId: {}, score: {}",
                response.getScoreId(), response.getScoreObtained());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //SS-2) 학생 점수 단건 조회를 위해 컨트롤러 내부에 선언된 CRUD(GET)
    @GetMapping("/{scoreId}")
    public ResponseEntity<StudentScoreResponseDTO> getStudentScore(@PathVariable Long scoreId) {

        log.info("1) 학생 점수 단건 조회 요청 - scoreId: {}", scoreId);

        StudentScoreResponseDTO response = scoreService.getStudentScore(scoreId);

        log.info("학생 점수 조회 완료 - scoreId: {}, score: {}",
                response.getScoreId(), response.getScoreObtained());

        return ResponseEntity.ok(response);
    }
    
    //SS-3) 학생별 모든 점수를 조회하기 위해 컨트롤러 내부에 선언된 CRUD(get)
    @GetMapping("/enrollments/{enrollmentId}")
    public ResponseEntity<List<StudentScoreResponseDTO>> getStudentScores(
            @PathVariable Long enrollmentId) {

        log.info("1) 학생별 점수 목록 조회 요청 - enrollmentId: {}", enrollmentId);

        List<StudentScoreResponseDTO> responses =
                scoreService.getStudentScores(enrollmentId);

        log.info("학생별 점수 조회 완료 - enrollmentId: {}, 점수 개수: {}",
                enrollmentId, responses.size());

        return ResponseEntity.ok(responses);
    }

    //SS-4) 평가항목별 모든 학생 점수를 조회하기 위해 컨트롤러 내부에 선언된 CRUD(get)
    @GetMapping("/items/{itemId}")
    public ResponseEntity<List<StudentScoreResponseDTO>> getItemScores(
            @PathVariable Long itemId,
            @RequestHeader(value = "X-User-Email", required = false) String professorEmail) {

        log.info("1) 평가항목별 점수 조회 요청 - itemId: {}, 교수: {}", itemId, professorEmail);

        // Postman 테스트용
        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            professorEmail = "hannah@aaa.com";
        }

        List<StudentScoreResponseDTO> responses =
                scoreService.getItemScores(itemId, professorEmail);

        log.info("평가항목별 점수 조회 완료 - itemId: {}, 점수 개수: {}",
                itemId, responses.size());

        return ResponseEntity.ok(responses);
    }

    //SS-5) 학생 점수를 수정하기 위해  컨트롤러 내부에 선언된 CRUD(PUT)
    @PutMapping("/{scoreId}")
    public ResponseEntity<StudentScoreResponseDTO> updateStudentScore(
            @PathVariable Long scoreId,
            @Valid @RequestBody StudentScoreUpdateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String professorEmail) {

        log.info("1) 학생 점수 수정 요청 - scoreId: {}, 교수: {}", scoreId, professorEmail);

        // Postman 테스트용
        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            professorEmail = "julia@aaa.com";
        }

        StudentScoreResponseDTO response =
                scoreService.updateStudentScore(scoreId, dto, professorEmail);

        log.info("학생 점수 수정 완료 - scoreId: {}, score: {}",
                response.getScoreId(), response.getScoreObtained());

        return ResponseEntity.ok(response);
    }

    //SS-6) 모든 점수가 입력 되었는지 확인하기 위해 컨트롤러 내부에 선언된 CRUD(get)
    @GetMapping("/enrollments/{enrollmentId}/offerings/{offeringId}/check-completion")
    public ResponseEntity<Map<String, Object>> checkAllScoresSubmitted(
            @PathVariable Long enrollmentId,
            @PathVariable Long offeringId) {

        log.info("1) 점수 입력 완료 확인 요청 - enrollmentId: {}, offeringId: {}",
                enrollmentId, offeringId);

        boolean isComplete = scoreService.checkAllScoreSubmitted(enrollmentId, offeringId);

        Map<String, Object> response = new HashMap<>();
        response.put("enrollmentId", enrollmentId);
        response.put("offeringId", offeringId);
        response.put("isComplete", isComplete);

        if (isComplete) {
            response.put("message", "모든 평가항목의 점수가 입력되었습니다");
            log.info("점수 입력 완료 - enrollmentId: {}", enrollmentId);
        } else {
            response.put("message", "아직 입력되지 않은 평가항목이 있습니다");
            log.warn("점수 입력 미완료 - enrollmentId: {}", enrollmentId);
        }

        return ResponseEntity.ok(response);
    }

    //SS-7) 학생 점수의 개수를 조회하기 위해 컨트롤러 내부에 선언된 CRUD(get)
    @GetMapping("/enrollments/{enrollmentId}/count")
    public ResponseEntity<Map<String, Object>> countStudentScores(
            @PathVariable Long enrollmentId) {

        log.info("1) 학생 점수 개수 조회 요청 - enrollmentId: {}", enrollmentId);

        Long count = scoreService.countStudentScore(enrollmentId);

        Map<String, Object> response = new HashMap<>();
        response.put("enrollmentId", enrollmentId);
        response.put("count", count);

        log.info("학생 점수 개수 조회 완료 - enrollmentId: {}, count: {}", enrollmentId, count);

        return ResponseEntity.ok(response);
    }
}
