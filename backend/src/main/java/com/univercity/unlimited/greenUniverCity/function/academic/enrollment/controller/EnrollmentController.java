package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/enrollment")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public String getEmail(String requesterEmail) {
        String email = requesterEmail;

        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com";
        }

        return email;
    }

    @GetMapping("/all2")
    public List<EnrollmentResponseDTO> postmanTestEnrollment(){
        List<EnrollmentResponseDTO> list=enrollmentService.legacyFindAllEnrollment();
        return list;
    }

    // E-1) Enrollment 테이블에 존재하는 모든 Enrollment 를 조회
    @GetMapping("/all")
    public List<EnrollmentResponseDTO> getEnrollmentResponseDTOList() {
        log.info("1) 여기는 Course 전체조회 Controller 입니다.");
        return enrollmentService.findAllEnrollment();
    }

    // E-2) Course의 Id를 통해 특정 데이터를 조회
    @GetMapping("/one/{enrollmentId}")
    public List<EnrollmentResponseDTO> getEnrollmentResponseDTOByEnrollmentId(
            @PathVariable("enrollmentId") Long enrollmentId)
    {
        log.info("1) 여기는 Enrollment 단일조회 Controller 입니다.");
        return enrollmentService.findById(enrollmentId);
    }

    // E-3) CourseCreateDTO를 통해 데이터를 생성
    @PostMapping("/create")
    public ResponseEntity<EnrollmentResponseDTO> createEnrollment(
            @RequestBody EnrollmentCreateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    )
    {
        log.info("1) Enrollment 추가 요청 {}", dto);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        EnrollmentResponseDTO createRespone = enrollmentService.createEnrollmentByAuthorizedUser(dto, email);
        return ResponseEntity.ok(createRespone);
    }

    // E-4) EnrollmentUpdateDTO를 통해 데이터를 갱신
    @PutMapping("/update")
    public ResponseEntity<EnrollmentResponseDTO> updateEnrollment(
            @RequestBody EnrollmentUpdateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    )
    {
        log.info("1) Enrollement 수정 요청 {}", dto);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        EnrollmentResponseDTO updateRespone = enrollmentService.updateEnrollmentByAuthorizedUser(dto,email);
        return ResponseEntity.ok(updateRespone);
    }

    // E-5) EnrollmentId 통해 데이터를 삭제 혹은 비활성화
    @DeleteMapping("/delete/{enrollmentId}")
    public ResponseEntity<String> deleteEnrollment(
            @PathVariable("enrollmentId") Long enrollmentId,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    ) {
        log.info("1) Enrollment 삭제 요청 {}", enrollmentId);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        Map<String,String> result = enrollmentService.deleteByEnrollmentId(enrollmentId,email);
        return ResponseEntity.ok(result.get("result"));
    }

    @GetMapping("/my")
    public ResponseEntity<Map<CourseOfferingResponseDTO, List<UserResponseDTO>>> getMyData(
            @RequestHeader(value = "X-User-Email", required = false) String professorEmail) {

        log.info("1) 교수가 관리하는 학생의 데이터 조회 요청 :{}", professorEmail);

        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            professorEmail = "hannah@aaa.com";
        }

        Map<CourseOfferingResponseDTO, List<UserResponseDTO>> response =
                enrollmentService.findAllStudentsByProfessorEmail(professorEmail);

        log.info("데이터 확인 : {}",response);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/myenroll")
    public ResponseEntity<List<EnrollmentResponseDTO>> getMyEnrollments(
            @RequestHeader("X-User-Email") String email
    ) {
        // email로 userId 찾아서 조회
        return ResponseEntity.ok(
                enrollmentService.findMyEnrollments(email)
        );
    }
}
