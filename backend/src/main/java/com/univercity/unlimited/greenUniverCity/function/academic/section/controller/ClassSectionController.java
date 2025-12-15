package com.univercity.unlimited.greenUniverCity.function.academic.section.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.service.ClassSectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/section")
@RequiredArgsConstructor
@Slf4j
public class ClassSectionController {
    private final ClassSectionService sectionService;

    //SE-1 분반에 대한 전체 정보를 조회하기 위해 컨트롤러 내에 선언된 CRUD(GET)
    @GetMapping("/all")
    public List<ClassSectionResponseDTO> postmanTestSection(){
        log.info("1) 분반 전체 조회 요청");
        return sectionService.findAllSection();
    }

    //SE-2) 특정 '강의(Offering)'에 속한 분반 목록을 조회하기 위해 컨트롤러 내에 선언된 CRUD(GET)
    @GetMapping("/list/offering/{offeringId}")
    public List<ClassSectionResponseDTO> postmanTestOneSection(@PathVariable("offeringId") Long offeringId){
        log.info("1) 특정 분반에 대한 정보 조회 요청");
        return sectionService.findSectionsByOfferingId(offeringId);
    }

    //SE-3) 새로운 분반에 대한 정보를 입력하여 생성하기 위해 컨트롤러 내에 선언된 CRUD(POST)
    @PostMapping("/create")
    public ResponseEntity<ClassSectionResponseDTO> postmanCreateSection(
            @Valid @RequestBody ClassSectionCreateDTO dto,
            @RequestHeader(value = "X-User-Email",required = false) String email){

        log.info("1) 분반 생성 요청 - 신원:{}",email);

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com"; // 테스트용 기본값
        }

        ClassSectionResponseDTO response=sectionService.createSection(dto,email);

        return ResponseEntity.ok(response);
    }

    //SE-4) 기존에 존재하는 분반에 대한 정보를 수정하기 위해 컨트롤러 내에 선언된 CRUD(PUT)
    @PutMapping("/update")
    public ResponseEntity<ClassSectionResponseDTO> postmanUpdateSection(
            @Valid @RequestBody ClassSectionUpdateDTO dto,
            @RequestHeader (value="X-User-Email",required = false) String email){

        log.info("1) 분반 정보 수정 요청 - sectionId-:{}, 신원-:{}",dto.getSectionId(),email);

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com"; // 테스트용 기본값
        }

        ClassSectionResponseDTO response=sectionService.updateSection(dto,email);

        return ResponseEntity.ok(response);
    }

    //SE-5) 분반에 대한 정보를 삭제하기 위해 컨트롤러 내에 선언된 CRUD(DELETE)
    @DeleteMapping("/delete/{sectionId}")
    public void postmanDeleteSection(
            @PathVariable("sectionId") Long sectionId,
            @RequestHeader (value="X-User-Email",required = false) String email){

        log.info("1) 분반 삭제 요청 - sectionId-:{}, 신원-:{}",sectionId,email);

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com"; // 테스트용 기본값
        }

        sectionService.deleteSection(sectionId,email);
    }

    //SE-6)웹에서 '한건조회' 버튼을 누를 때 이 API를호출
    @GetMapping("/one/{sectionId}")
    public ResponseEntity<ClassSectionResponseDTO> getSectionDetail(@PathVariable("sectionId") Long sectionId) {
        log.info("1) 특정 분반(sectionId: {}) 상세 조회 요청", sectionId);

        ClassSectionResponseDTO response = sectionService.getSection(sectionId);

        return ResponseEntity.ok(response);
    }




}
