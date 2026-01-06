package com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.service.AcademicTermService;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/term")
public class AcademicTermController {

    private final AcademicTermService academicTermService;
    private final AcademicSecurityValidator securityValidator;

    private final String baseName = "AcademicTermController";

    /// ========================================
    ///         T-1) 전체 조회
    ///         누구나 가능 (권한 체크 없음)
    /// ========================================
    @GetMapping("/all")
    public List<AcademicTermResponseDTO> getTermResponseDTOList(
            @RequestHeader(value = "X-User-Email", required = true) String requesterEmail
    ) {
        log.info("{} 전체 조회 시도 - 요청자: {}", baseName, requesterEmail);
        // 조회는 누구나 가능 (권한 체크 없음)
        return academicTermService.findAllTerm();
    }

    /// ========================================
    ///         T-2) 단 건 조회
    ///         누구나 가능 (권한 체크 없음)
    /// ========================================
    @GetMapping("/one/{termId}")
    public AcademicTermResponseDTO getTermResponseDTOByTermId(
            @PathVariable Long termId,
            @RequestHeader(value = "X-User-Email", required = true) String requesterEmail
    ) {
        log.info("{} 단 건 조회 시도 - termId: {}, 요청자: {}", baseName, termId, requesterEmail);
        return academicTermService.findById(termId);
    }

    /// ========================================
    ///         T-3) 단 건 생성
    ///         관리자만 가능
    /// ========================================
    @PostMapping("/create")
    public ResponseEntity<AcademicTermResponseDTO> createTerm(
            @RequestBody AcademicTermCreateDTO dto,
            @RequestHeader(value = "X-User-Email", required = true) String requesterEmail
    ) {
        log.info("{} 생성 시도 - 요청자: {}", baseName, requesterEmail);
        securityValidator.validateAdminRole(requesterEmail, baseName + " 생성");

        AcademicTermResponseDTO createResponse = academicTermService.createTerm(dto);
        return ResponseEntity.ok(createResponse);
    }

    /// ========================================
    ///         T-4) 단 건 수정
    ///         관리자만 가능
    /// ========================================
    @PutMapping("/update")
    public ResponseEntity<AcademicTermResponseDTO> updateTerm(
            @RequestBody AcademicTermUpdateDTO dto,
            @RequestHeader(value = "X-User-Email", required = true) String requesterEmail
    ) {
        log.info("{} 수정 시도 - 요청자: {}", baseName, requesterEmail);
        securityValidator.validateAdminRole(requesterEmail, "Term 수정");

        AcademicTermResponseDTO updateResponse = academicTermService.updateTerm(dto);
        return ResponseEntity.ok(updateResponse);
    }

    /// ========================================
    ///         T-5) 단 건 삭제
    ///         관리자만 가능
    /// ========================================
    @DeleteMapping("/delete/{termId}")
    public ResponseEntity<String> deleteTerm(
            @PathVariable Long termId,
            @RequestHeader(value = "X-User-Email", required = true) String requesterEmail
    ) {
        log.info("{} 삭제 시도 - termId: {}, 요청자: {}", baseName, termId, requesterEmail);
        securityValidator.validateAdminRole(requesterEmail, "Term 삭제");

        Map<String, String> result = academicTermService.deleteTerm(termId);
        return ResponseEntity.ok(result.get("Result"));
    }
}
