package com.univercity.unlimited.greenUniverCity.function.academic.grade.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.service.GradeItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/grade-item")
public class GradeItemController {
    private final GradeItemService itemService;

    //GI-1) 교수가 평가 항목을 생성하기 위해 컨트롤러 내에 선언된 CRUD(Post) - 확인완
    @PostMapping("/create")
    public ResponseEntity<GradeItemResponseDTO> postmanCreateItem(
            @Valid @RequestBody GradeItemCreateDTO dto,
            @RequestHeader(value = "X-User-Email",required = false) String professorEmail){

        log.info("1) 평가항목 생성 요청 - 교수:{}, offeringId-:{}", professorEmail, dto.getOfferingId());

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            professorEmail = "hannah@aaa.com"; // 테스트용 기본값
        }

        GradeItemResponseDTO response= itemService.createGradeItem(dto,professorEmail);

        log.info("Complete:평가항목 생성 완료 - itemId-:{}", response.getItemId());

        return ResponseEntity.ok(response);
    }

    //GI-2) 평가항목 테이블에 존재하는 데이터 하나를 단건 조회하기 위해 컨트롤러 내에 선언된 Crud(Get)  - 확인완
    @GetMapping("/one/{itemId}")
    public ResponseEntity<GradeItemResponseDTO> getGradeItem(@PathVariable Long itemId) {
        log.info("1) 평가항목 단건 조회 요청 - ItemId: {}", itemId);

        GradeItemResponseDTO response = itemService.getGradeItem(itemId);

        log.info("Complete: 조회 완료");
        return ResponseEntity.ok(response);
    }

    //GI-3) 특정 강의에 존재하는 평가항목 목록을 조회하기 위해 컨트롤러 내에 선언된 CRud(get) - 확인완
    @GetMapping("/offering/{offeringId}")
    public ResponseEntity<List<GradeItemResponseDTO>> postmanTestOfferingItem(@PathVariable("offeringId") Long offeringId){
        log.info("1) 특정 목록에 존재하는 데이터 조회 요청 - offeringId-:{}",offeringId);

        List<GradeItemResponseDTO> responses = itemService.getOfferingGradeItem(offeringId);

        return ResponseEntity.ok(responses);
    }

    //GI-4) 기존에 존재하던 평가항목에 대한 정보를 수정하기 위해 컨트롤러 내에 선언된 CRUD(PUT) -확인완
    @PutMapping("/update")
    public ResponseEntity<GradeItemResponseDTO> updateItem(
            @Valid @RequestBody GradeItemUpdateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String professorEmail) {

        // DTO 안에 있는 itemId를 꺼내서 로그를 찍음
        log.info("1) 평가항목 수정 요청 - ItemId: {}, 교수: {}", dto.getItemId(), professorEmail);

        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더 누락. 테스트 계정 사용");
            professorEmail = "hannah@aaa.com";
        }

        // Service 호출 시 DTO 안의 ID를 첫 번째 인자로 전달
        GradeItemResponseDTO response = itemService.updateGradeItem(dto.getItemId(), dto, professorEmail);

        return ResponseEntity.ok(response);
    }


}
