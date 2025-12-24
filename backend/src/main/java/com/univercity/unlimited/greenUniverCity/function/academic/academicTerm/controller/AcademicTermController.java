package com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.service.AcademicTermService;
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

    public String getEmail(String requesterEmail) {
        String email = requesterEmail;

        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com";
        }

        return email;
    }

    // T-1) Term 테이블에 존재하는 모든 Term 조회
    @GetMapping("/all")
    public List<AcademicTermResponseDTO> getTermResponseDTOList() {
        log.info("1) Term 전체 조회 입니다.");
        return academicTermService.findAllTerm();
    }

    // T-2) Term Id를 통해 특정 데이터를 조회
    // 현재 프론트 테스트 페이지와의 연동을 위해 리스트를 통해 반환
    @GetMapping("/one/{termId}")
    public AcademicTermResponseDTO getTermResponseDTOByTermId(
            @PathVariable("termId") Long termId)
    {
        log.info("1) Term 한 건 조회 입니다.");
        return academicTermService.findById(termId);
    }

    // T-3) TermCreateDTO를 통해 데이터를 생성
    @PostMapping("/create")
    public ResponseEntity<AcademicTermResponseDTO> createTerm(
            @RequestBody AcademicTermCreateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    )
    {
        log.info("1) Term 한 건 생성 입니다. : {}", dto);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        AcademicTermResponseDTO createRespone = academicTermService.createTermByAuthorizedUser(dto,email);
        return ResponseEntity.ok(createRespone);
    }

    // T-4) TermUpdateDTO를 통해 데이터를 갱신
    @PutMapping("/update")
    public ResponseEntity<AcademicTermResponseDTO> updateTerm(
            @RequestBody AcademicTermUpdateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    )
    {
        log.info("1) Term 한 건 수정 입니다.");

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        AcademicTermResponseDTO updateRespone = academicTermService.updateTermByAuthorizedUser(dto, email);
        return ResponseEntity.ok(updateRespone);
    }

    // T-5) TermId를 통해 데이터를 삭제 혹은 비활성화
    @DeleteMapping("/delete/{termId}")
    public ResponseEntity<String> deleteTerm(
            @PathVariable("termId") Long termId,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    ) {
        log.info("1) Term 한 건 삭제 입니다.");

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        Map<String,String> result = academicTermService.deleteByTermId(termId,email);
        return ResponseEntity.ok(result.get("result"));
    }
}
