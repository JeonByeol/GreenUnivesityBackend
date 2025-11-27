package com.univercity.unlimited.greenUniverCity.function.course.controller;

import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.course.dto.LegacyCourseDTO;
import com.univercity.unlimited.greenUniverCity.function.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;

    public String getEmail(String requesterEmail) {
        String email = requesterEmail;

        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com";
        }

        return email;
    }

    // C-1) Course테이블에 존재하는 모든 Course를 조회
    @GetMapping("/all")
    public List<CourseResponseDTO> getCourseResponseDTOList() {
        log.info("1) 여기는 Course 전체조회 Controller 입니다.");
        return courseService.findAllCourse();
    }

    // TODO : one 조회인데, List로 반환하는 문제 해결
    // C-2) Course의 Id를 통해 특정 데이터를 조회
    // 현재 프론트 테스트 페이지와의 연동을 위해 리스트를 통해 반환
    @GetMapping("/one/{courseId}")
    public List<CourseResponseDTO> getCourseResponseDTOByCourseId(
            @PathVariable("courseId") Long courseId)
    {
        log.info("1) 여기는 Course 단일조회 Controller 입니다.");
        return courseService.findById(courseId);
    }

    // C-3) CourseCreateDTO를 통해 데이터를 생성
    @PostMapping("/create")
    public ResponseEntity<CourseResponseDTO> createCourse(
            @RequestBody CourseCreateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
            )
    {
        log.info("1) Course 추가 요청 {}", dto);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        CourseResponseDTO createRespone = courseService.createCourseByAuthorizedUser(dto, email);
        return ResponseEntity.ok(createRespone);
    }

    // C-4) CourseUpdateDTO를 통해 데이터를 갱신
    @PutMapping("/update")
    public ResponseEntity<CourseResponseDTO> updateCourse(
            @RequestBody CourseUpdateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    )
    {
        log.info("1) Course 수정 요청 {}", dto);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        CourseResponseDTO updateRespone = courseService.updateCourseByAuthorizedUser(dto,email);
        return ResponseEntity.ok(updateRespone);
    }

    // C-5) courseId를 통해 데이터를 삭제 혹은 비활성화
    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable("courseId") Long courseId,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail
    ) {
        log.info("1) Course 삭제 요청 {}", courseId);

        // 이메일이 비어있을 경우 처리도 고려
        String email = getEmail(requesterEmail);

        Map<String,String> result = courseService.deleteByCourseId(courseId,email);
        return ResponseEntity.ok(result.get("result"));
    }

}
