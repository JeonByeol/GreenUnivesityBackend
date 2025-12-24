package com.univercity.unlimited.greenUniverCity.function.academic.assignment.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment.AssignmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment.AssignmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment.AssignmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.service.AssignmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignment")
@RequiredArgsConstructor
@Slf4j
public class AssignmentController {

    private final AssignmentService assignmentService;
    
    // AS-ALL) 전체조회
    @GetMapping("/all")
    public ResponseEntity<List<AssignmentResponseDTO>> getAllAssignments() {
        log.info("API Request: 모든 과제 목록 조회 요청");

        List<AssignmentResponseDTO> result = assignmentService.findAllAssignments();

        return ResponseEntity.ok(result);
    }

    // AS-1) (공통) 특정 분반의 과제 목록 조회
    @GetMapping("/sections/{sectionId}")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsBySection(
            @PathVariable Long sectionId) {

        log.info("1) 분반별 과제 목록 조회 요청 - sectionId: {}", sectionId);

        List<AssignmentResponseDTO> response = assignmentService.findAssignmentsBySectionId(sectionId);

        return ResponseEntity.ok(response);
    }

    // AS-2) (교수) 과제 생성 (출제)
    @PostMapping("/create")
    public ResponseEntity<AssignmentResponseDTO> createAssignment(
            @Valid @RequestBody AssignmentCreateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String email) {

        log.info("1) 과제 생성 요청 - title: {}, 사용자: {}", dto.getTitle(), email);

        // 테스트용 기본값 (필요 시 제거)
        if (email == null || email.isEmpty()) email = "hannah@aaa.com";

        AssignmentResponseDTO response = assignmentService.createAssignment(dto, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // AS-3) (교수) 과제 수정
    @PutMapping("/update")
    public ResponseEntity<AssignmentResponseDTO> updateAssignment(
            @Valid @RequestBody AssignmentUpdateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String email) {

        log.info("1) 과제 수정 요청 - assignmentId: {}, 사용자: {}", dto.getAssignmentId(), email);

        if (email == null || email.isEmpty()) email = "hannah@aaa.com";

        AssignmentResponseDTO response = assignmentService.updateAssignment(dto, email);

        return ResponseEntity.ok(response);
    }

    // AS-4) (교수) 과제 삭제
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(
            @PathVariable Long assignmentId,
            @RequestHeader(value = "X-User-Email", required = false) String email) {

        log.info("1) 과제 삭제 요청 - assignmentId: {}, 사용자: {}", assignmentId, email);

        if (email == null || email.isEmpty()) email = "hannah@aaa.com";

        assignmentService.deleteAssignment(assignmentId, email);

        return ResponseEntity.ok("과제가 삭제되었습니다. (ID: " + assignmentId + ")");
    }

    // AS-5) (공통) 과제 상세 조회
    @GetMapping("/one/{assignmentId}")
    public ResponseEntity<AssignmentResponseDTO> getAssignment(@PathVariable Long assignmentId) {

        log.info("1) 과제 상세 조회 요청 - assignmentId: {}", assignmentId);

        AssignmentResponseDTO response = assignmentService.getAssignment(assignmentId);

        return ResponseEntity.ok(response);
    }
}