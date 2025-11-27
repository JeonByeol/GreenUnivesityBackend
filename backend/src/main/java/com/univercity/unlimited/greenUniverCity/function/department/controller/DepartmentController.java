package com.univercity.unlimited.greenUniverCity.function.department.controller;

import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.department.dto.DepartmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.department.dto.DepartmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.department.dto.DepartmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.department.dto.LegacyDepartmentDTO;
import com.univercity.unlimited.greenUniverCity.function.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    public String getEmail(String requesterEmail) {
        String email = requesterEmail;

        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com";
        }

        return email;
    }

    // D-1) Course테이블에 존재하는 모든 Course를 조회
    @GetMapping("/all")
    public List<DepartmentResponseDTO> getDepartmentResponseDTOList() {
        log.info("1) 여기는 Course 전체조회 Controller 입니다.");
        return departmentService.findAllDepartment();
    }

    // D-2) Course의 Id를 통해 특정 데이터를 조회
    // 현재 프론트 테스트 페이지와의 연동을 위해 리스트를 통해 반환
    @GetMapping("/one/{departmentId}")
    public List<DepartmentResponseDTO> getCourseResponseDTOByDepartmentId(
            @PathVariable("departmentId") Long departmentId)
    {
        log.info("1) 여기는 Course 단일조회 Controller 입니다.");
        return departmentService.findById(departmentId);
    }

    // C-3) CourseCreateDTO를 통해 데이터를 생성
    @PostMapping("/create")
    public ResponseEntity<DepartmentResponseDTO> createCourse(
            @RequestBody DepartmentCreateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    )
    {
        log.info("1) Department 추가 요청 {}", dto);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        DepartmentResponseDTO createRespone = departmentService.createDepartmentByAuthorizedUser(dto, email);
        return ResponseEntity.ok(createRespone);
    }

    // C-4) CourseUpdateDTO를 통해 데이터를 갱신
    @PutMapping("/update")
    public ResponseEntity<DepartmentResponseDTO> updateCourse(
            @RequestBody DepartmentUpdateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    )
    {
        log.info("1) Course 수정 요청 {}", dto);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        DepartmentResponseDTO updateRespone = departmentService.updateDepartmentByAuthorizedUser(dto,email);
        return ResponseEntity.ok(updateRespone);
    }

    // C-5) courseId를 통해 데이터를 삭제 혹은 비활성화
    @DeleteMapping("/delete/{departmentId}")
    public ResponseEntity<String> deleteDepartment(
            @PathVariable("departmentId") Long departmentId,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    ) {
        log.info("1) Department 삭제 요청 {}", departmentId);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        Map<String,String> result = departmentService.deleteByDepartmentId(departmentId,email);
        return ResponseEntity.ok(result.get("result"));
    }
}
