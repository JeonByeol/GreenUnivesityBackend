package com.univercity.unlimited.greenUniverCity.function.academic.assignment.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission.SubmissionCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission.SubmissionResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission.SubmissionUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/submission")
@RequiredArgsConstructor
@Slf4j
public class SubmissionController {

    private final SubmissionService submissionService;
    
    //SB-ALL)전체조회
    @GetMapping("/all")
    public ResponseEntity<List<SubmissionResponseDTO>> getAllSubmissions() {
        log.info("API Request: 모든 제출 내역 목록 조회 요청");

        List<SubmissionResponseDTO> result = submissionService.findAllSubmissions();

        return ResponseEntity.ok(result);
    }

    // SB-1) (학생) 과제 제출
    @PostMapping("/create")
    public ResponseEntity<SubmissionResponseDTO> submitAssignment(
            @Valid @RequestBody SubmissionCreateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String email) {

        log.info("1) 과제 제출 요청 - assignmentId: {}, 사용자: {}", dto.getAssignmentId(), email);

        // 테스트용 (학생 계정)
        if (email == null || email.isEmpty()) email = "student1@aaa.com";

        SubmissionResponseDTO response = submissionService.submitAssignment(dto, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // SB-2) (학생) 과제 재제출 (파일 수정)
    @PutMapping("/update")
    public ResponseEntity<SubmissionResponseDTO> updateSubmissionFile(
            @Valid @RequestBody SubmissionUpdateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String email) {

        log.info("1) 과제 재제출 요청 - submissionId: {}", dto.getSubmissionId());

        if (email == null || email.isEmpty()) email = "student1@aaa.com";

        SubmissionResponseDTO response = submissionService.updateSubmissionFile(dto, email);

        return ResponseEntity.ok(response);
    }

    // SB-3) (교수) 과제 채점
    // 채점 점수는 Body로 받음 ({ "score": 95.5 })
    @PutMapping("/{submissionId}/grade")
    public ResponseEntity<SubmissionResponseDTO> gradeSubmission(
            @PathVariable Long submissionId,
            @RequestBody Map<String, Float> body,
            @RequestHeader(value = "X-User-Email", required = false) String email) {

        Float score = body.get("score");
        log.info("1) 과제 채점 요청 - submissionId: {}, 점수: {}, 교수: {}", submissionId, score, email);

        if (email == null || email.isEmpty()) email = "hannah@aaa.com";

        if (score == null) {
            throw new IllegalArgumentException("점수(score)는 필수입니다.");
        }

        SubmissionResponseDTO response = submissionService.gradeSubmission(submissionId, score, email);

        return ResponseEntity.ok(response);
    }

    // SB-4) (교수) 특정 과제 제출물 전체 조회
    @GetMapping("/assignments/{assignmentId}")
    public ResponseEntity<List<SubmissionResponseDTO>> getSubmissionsByAssignment(
            @PathVariable Long assignmentId) {

        log.info("1) 과제별 제출물 조회 요청 - assignmentId: {}", assignmentId);

        List<SubmissionResponseDTO> response = submissionService.findAllSubmissionsByAssignment(assignmentId);

        return ResponseEntity.ok(response);
    }

    // SB-5) (학생) 내 제출 내역 확인
    @GetMapping("/one/{assignmentId}")
    public ResponseEntity<SubmissionResponseDTO> getMySubmission(
            @PathVariable Long assignmentId,
            @RequestHeader(value = "X-User-Email", required = false) String email) {

        log.info("1) 내 과제 제출 내역 조회 - assignmentId: {}, 사용자: {}", assignmentId, email);

        if (email == null || email.isEmpty()) email = "student1@aaa.com";

        SubmissionResponseDTO response = submissionService.findMySubmission(assignmentId, email);

        // 미제출 시 204 No Content 혹은 null 반환 (여기선 ok(null)로 처리하여 프론트에서 판단)
        return ResponseEntity.ok(response);
    }
}