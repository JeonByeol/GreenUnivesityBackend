package com.univercity.unlimited.greenUniverCity.function.academic.classroom.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.service.ClassroomService;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/classroom")
public class ClassroomController {
    private final ClassroomService classroomService;

    //CR-1) 클래스 룸 테이블에 존재하는 모든 강의실 정보를 조회하기 위해 컨틀롤러 내에 선언된 CRUD(GET)
    @GetMapping("/all")
    public List<ClassroomResponseDTO> postmanTestClassroom(){
        log.info("1) 강의실 전체 조회 요청");
        return classroomService.findAllClassroom();
    }

    //CR-2) 특정 강의실에 대한 정보를 조회하기 위해 컨트롤러 내에 선언된 crud(get)
    @GetMapping("/one/{keyword}")
    public List<ClassroomResponseDTO> postmanTestOneClassroom(@PathVariable("keyword") String keyword){
        log.info("1) 특정 강의실 조회 요청");
        return classroomService.findByLocation(keyword);
    }

    //CR-2-1)웹에서 '한건조회' 버튼을 누를 때 이 API를호출
    @GetMapping("/one/{classroomId}")
    public ResponseEntity<ClassroomResponseDTO> getClassroom(@PathVariable("classroomId") Long classroomId) {
        log.info("1) 특정 분반(sectionId: {}) 상세 조회 요청", classroomId);

        ClassroomResponseDTO response = classroomService.getRoom(classroomId);

        return ResponseEntity.ok(response);
    }

    //CR-3) 새로운 강의실에 대한 정보를 입력하여 생성하기 위해 컨트롤러 내에 선언된 CRUD(POST)
    @PostMapping("/create")
    public ResponseEntity<ClassroomResponseDTO> postmanCreateRoom(
            @Valid @RequestBody ClassroomCreateDTO dto,
            @RequestHeader (value = "X-User-Email",required = false) String email){
        
        log.info("1) 강의실 생성 요청 - 신원-:{}",email);

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com"; // 테스트용 기본값
        }

        ClassroomResponseDTO response=classroomService.createClassroom(dto,email);

        return ResponseEntity.ok(response);
    }

    //CR-4) 기존에 존재하는 강의실에 대한 정보를 수정하기 위해 컨트롤러 내에 선언된 CRUD(PUT)
    @PutMapping("/update")
    public ResponseEntity<ClassroomResponseDTO> postmanUpdateClassroom(
            @Valid @RequestBody ClassroomUpdateDTO dto,
            @RequestHeader (value="X-User-Email",required = false) String email){

        log.info("1) 강의실 정보 수정 요청 - classroomId-:{}, 신원-:{}",dto.getClassroomId(),email);

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com"; // 테스트용 기본값
        }

        ClassroomResponseDTO response=classroomService.updateClassroom(dto,email);

        return ResponseEntity.ok(response);
    }

    //CR-5) 강의실에 대한 정보를 삭제하기 위해 컨트롤러 내에 선언된 CRUD(DELETE)

    @DeleteMapping("/delete/{classroomId}")
    public void postmanDeleteClassroom(
            @PathVariable("classroomId") Long classroomId,
            @RequestHeader (value="X-User-Email",required = false) String email) {

        log.info("1) 강의실 삭제 요청 - classroomId-:{}, 신원-:{}",classroomId,email);

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com"; // 테스트용 기본값
        }

        classroomService.deleteByClassroom(classroomId,email);
    }

}
