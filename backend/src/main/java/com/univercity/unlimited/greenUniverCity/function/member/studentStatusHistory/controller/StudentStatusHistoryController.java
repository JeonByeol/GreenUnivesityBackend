package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.service.StudentStatusHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/sshistory")
public class StudentStatusHistoryController {
    private final StudentStatusHistoryService studentStatusHistoryService;

    public String getEmail(String requesterEmail) {
        String email = requesterEmail;

        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "root@aaa.com";
        }

        return email;
    }

    // H-1) StudentStatusHistory 테이블에 존재하는 모든 데이터 조회
    @GetMapping("/all")
    public List<StudentStatusHistoryResponseDTO> getHistoryDTOList() {
        log.info("1) History 조회 ");
        return studentStatusHistoryService.findAllHistory();
    }

    // H-2) StudentStatusHistory의 id를 통해 특정 데이터를 조회
    @GetMapping("/one/{historyId}")
    public StudentStatusHistoryResponseDTO getHistoryResponseDTOByHistoryId(
            @PathVariable("historyId") Long historyId)
    {
        log.info("1) 여기는 History 단일조회 Controller 입니다.");
        return studentStatusHistoryService.findById(historyId);
    }

    // H-3) StudentStatusHistoryCreateDTO를 통해 데이터 생성
    @PostMapping("/create")
    public ResponseEntity<StudentStatusHistoryResponseDTO> createHistory(
            @RequestBody StudentStatusHistoryCreateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    )
    {
        log.info("1) History 추가 요청 {}", dto);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        StudentStatusHistoryResponseDTO createRespone = studentStatusHistoryService.createHistoryByAuthorizedUser(dto, email);
        return ResponseEntity.ok(createRespone);
    }

    // H-4) StudentStatusHistoryUpdateDTO를 통해 데이터 갱신
    @PutMapping("/update")
    public ResponseEntity<StudentStatusHistoryResponseDTO> update(
            @RequestBody StudentStatusHistoryUpdateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    )
    {
        log.info("1) History 수정 요청 {}", dto);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        StudentStatusHistoryResponseDTO updateRespone = studentStatusHistoryService.updateHistoryByAuthorizedUser(dto,email);
        return ResponseEntity.ok(updateRespone);
    }

    // H-5) StudentStatusHistory의 id를 통해 특정 데이터 삭제
    @DeleteMapping("/delete/{historyId}")
    public ResponseEntity<String> deleteHistory(
            @PathVariable("historyId") Long historyId,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    ) {
        log.info("1) Offering 삭제 요청 {}", historyId);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        Map<String,String> result = studentStatusHistoryService.deleteByHistoryId(historyId,email);
        return ResponseEntity.ok(result.get("result"));
    }

    @PostMapping("/approved")
    public ResponseEntity<StudentStatusHistoryResponseDTO> appovedSSHistory(
            @RequestBody StudentStatusHistoryUpdateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail) {
        StudentStatusHistoryResponseDTO responseDTO = studentStatusHistoryService.approveStatusHistory(dto,requesterEmail);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/rejected")
    public ResponseEntity<StudentStatusHistoryResponseDTO> rejectedSSHistory(
            @RequestBody StudentStatusHistoryUpdateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail) {
        StudentStatusHistoryResponseDTO responseDTO = studentStatusHistoryService.rejectStatusHistory(dto,requesterEmail);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/my")
    public ResponseEntity<List<StudentStatusHistoryResponseDTO>> getMyData(
            @RequestHeader(value = "X-User-Email", required = false) String studentEmail) {

        // 1) 요청 로그
        log.info("1) 학생의 본인 데이터 조회 요청 - 학생-:{}", studentEmail);

        // 2) 이메일 검증 (Postman 테스트용)
        if (studentEmail == null || studentEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            studentEmail = "hannah@aaa.com";
        }

        // 3) 서비스 호출
        List<StudentStatusHistoryResponseDTO> response =
                studentStatusHistoryService.getMyData(studentEmail);

        // 4) 완료 로그
        log.info("Complete: 학생 데이터 조회 완료 - 학생-:{}, 데이터개수-:{}",
                studentEmail, response.size());

        // 5) 응답 반환
        return ResponseEntity.ok(response);
    }

}
