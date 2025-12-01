package com.univercity.unlimited.greenUniverCity.function.academic.offering.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/offering")
public class CourseOfferingController {
    private final CourseOfferingService courseOfferingService;

    public String getEmail(String requesterEmail) {
        String email = requesterEmail;

        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com";
        }

        return email;
    }

    // CO-1) Offering 테이블에 존재하는 모든 Offering 을 조회
    @GetMapping("/all")
    public List<CourseOfferingResponseDTO> getOfferingResponseDTOList() {
        log.info("1) 여기는 Offering 전체조회 Controller 입니다.");
        return courseOfferingService.findAllOffering();
    }

    // CO-2) Offering 의 Id를 통해 특정 데이터를 조회
    @GetMapping("/one/{offeringId}")
    public List<CourseOfferingResponseDTO> getCourseResponseDTOByOfferingId(
            @PathVariable("offeringId") Long offeringId)
    {
        log.info("1) 여기는 Offering 단일조회 Controller 입니다.");
        return courseOfferingService.findById(offeringId);
    }

    // CO-3) OfferingCreateDTO 를 통해 데이터를 생성
    @PostMapping("/create")
    public ResponseEntity<CourseOfferingResponseDTO> createOffering(
            @RequestBody CourseOfferingCreateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    )
    {
        log.info("1) Offering 추가 요청 {}", dto);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        CourseOfferingResponseDTO createRespone = courseOfferingService.createOfferingByAuthorizedUser(dto, email);
        return ResponseEntity.ok(createRespone);
    }

    // CO-4) OfferingUpdateDTO를 통해 데이터를 갱신
    @PutMapping("/update")
    public ResponseEntity<CourseOfferingResponseDTO> update(
            @RequestBody CourseOfferingUpdateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    )
    {
        log.info("1) Offering 수정 요청 {}", dto);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        CourseOfferingResponseDTO updateRespone = courseOfferingService.updateOfferingByAuthorizedUser(dto,email);
        return ResponseEntity.ok(updateRespone);
    }

    // CO-5) offeringId를 통해 데이터를 삭제 혹은 비활성화
    @DeleteMapping("/delete/{offeringId}")
    public ResponseEntity<String> deleteOffering(
            @PathVariable("offeringId") Long offeringId,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    ) {
        log.info("1) Offering 삭제 요청 {}", offeringId);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        Map<String,String> result = courseOfferingService.deleteByOfferingId(offeringId,email);
        return ResponseEntity.ok(result.get("result"));
    }
}
