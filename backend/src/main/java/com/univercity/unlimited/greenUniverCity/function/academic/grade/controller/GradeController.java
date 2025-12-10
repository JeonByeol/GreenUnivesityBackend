package com.univercity.unlimited.greenUniverCity.function.academic.grade.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.LegacyGradeDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/grade")
public class GradeController {
    private final GradeService gradeService;

    //G-1) 성적 테이블에 존재하는 전체 데이터 조회
    @GetMapping("/all")
    public List<LegacyGradeDTO> postmanTestGrade(){
        log.info("Controller: 성적전체조회");
        return gradeService.findAllGrade();
    }

    //G-2) 특정 학생이 본인이 수강한 모든 과목의 성적과 과목명을 조회
    @GetMapping("/mygrade/{email}")
    public List<GradeResponseDTO> postmanMyGrade(@PathVariable("email") String email){
       return gradeService.myGrade(email);
    }

    //G-3) 교수가 특정 과목의 수업을 듣는 전체학생 조회
    @GetMapping("/course/{offeringId}")
    public List<GradeResponseDTO> postmanCourseGrade(@PathVariable("offeringId") Long offeringId){
        return gradeService.offeringOfGrade(offeringId);
    }

    //G-4) 교수가 특정 학생에 대한 성적을 수정(입력)
    @PutMapping("{enrollmentId}")
    public ResponseEntity<LegacyGradeDTO> updateGrade(
            @PathVariable("enrollmentId") Long enrollmentId,
            @RequestBody LegacyGradeDTO legacyGradeDTO) {

        String gradeValue= legacyGradeDTO.getGradeValue();

        log.info(" 수강신청 ID[{}]의 성적을 [{}]로 입력 시도...",  enrollmentId, gradeValue);

        LegacyGradeDTO updateGrade=gradeService.updateNewGrade(
                enrollmentId,
                gradeValue
        );
        return ResponseEntity.ok(updateGrade);
    }
    //NEW-G-1) 교수가 학생에 대한 최종 성적을 생성하기 위해 컨트롤러 내부에 선언된 crud(post)
    @PostMapping("/create")
    public ResponseEntity<GradeResponseDTO> createGrade(
            @Valid @RequestBody GradeCreateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String professorEmail){

        log.info("1) 최종 성적 생성 요청 - enrollmentId-:{}, 교수-:{}", dto.getEnrollmentId(), professorEmail);

        // Postman 테스트용
        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            professorEmail = "hannah@aaa.com";
        }

        GradeResponseDTO response= gradeService.createGrade(dto,professorEmail);

        log.info("Complete:최종 성적 생성 완료 - gradeId-:{}, letterGrade-:{}",
                response.getGradeId(), response.getLetterGrade());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //NEW-G-2) 최종 성적에 대한 단건 조회를 하기 위해 컨트롤러 내부에 선언된 CRUD(get)
    @GetMapping("/{gradeId}")
    public ResponseEntity<GradeResponseDTO> getGrade(@PathVariable Long gradeId){

        log.info("1) 최종 성적 단건 조회 요청 - gradeId-:{}",gradeId);

        GradeResponseDTO response= gradeService.getGrade(gradeId);

        log.info("Complete: 최종 성적 조회 완료 - gradeId-:{}, letterGrade-:{}",
                response.getGradeId(), response.getLetterGrade());

        return ResponseEntity.ok(response);
    }

    //NEW-G-3) 학생의 모든 성적을 조회하기 위해 컨트롤러 내부에 선언된 Crud(get)
    @GetMapping("/students/my-grades")
    public ResponseEntity<List<GradeResponseDTO>> getMyGrade(
            @RequestHeader(value = "X-User-Email", required = false) String studentEmail){

        log.info("1) 학생의 본인 성적 조회 요청 - 학생-:{}", studentEmail);

        // Postman 테스트용
        if (studentEmail == null || studentEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            studentEmail = "hannah@aaa.com";
        }
        
        //뭔가 뭔가 이상함 추후 수정 예정
        List<GradeResponseDTO> response=gradeService.getStudentGrades(studentEmail,studentEmail);

        log.info("Complete: 학생 성적 조회 완료 - 학생-:{}, 성적개수-:{}",
                 studentEmail, response.size());

        return ResponseEntity.ok(response);
    }

    //New-G-4) 강의별 모든 학생의 성적을 조회하기 위해 컨트롤러 내부에 선언된 crud(get)
    @GetMapping("offerings/{offeringId}")
    public ResponseEntity<List<GradeResponseDTO>> getOfferingGrades(
            @PathVariable Long offeringId,
            @RequestHeader (value = "X-User-Email", required = false) String professorEmail){

        log.info("1) 강의별 성적 조회 요청 - offeringId-:{}, 교수-:{}", offeringId, professorEmail);

        // Postman 테스트용
        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            professorEmail = "hannah@aaa.com";
        }

        List<GradeResponseDTO> responses= gradeService.getOfferingGrades(offeringId, professorEmail);

        log.info("Complete: 강의별 성적 조회 완료 - offeringId-:{}, 성적개수-:{}",
                 offeringId, responses.size());

        return ResponseEntity.ok(responses);
    }






//    @PostMapping("/test")
//    public GradeDTO postmanTestAdd(@RequestBody GradeDTO dto){
//        Long enrollment=dto.getEnrollment().getEnrollmentId();
//        String value=dto.getGradeValue();
//        String mail=dto.getEnrollment().getUser().getEmail();
//        return gradeService.postNewGrade(enrollment,value,mail);
//    }
}
