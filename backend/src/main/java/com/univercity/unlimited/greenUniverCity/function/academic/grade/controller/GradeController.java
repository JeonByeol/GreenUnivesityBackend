package com.univercity.unlimited.greenUniverCity.function.academic.grade.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeUpdateDTO;
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

    //NEW-G-1) 최종 성적 테이블에 존재하는 모든 데이터를 조회하기 위해 컨트롤러 내부에 선언된 crud(get)
    @GetMapping("/all")
    public ResponseEntity<List<GradeResponseDTO>> getGrades(){
        log.info("1) 최종 성적 전체조회 요청");

        return ResponseEntity.ok(gradeService.findAllGrades());
    }

    //NEW-G-2) 최종 성적에 대한 단건 조회를 하기 위해 컨트롤러 내부에 선언된 CRUD(get)
    @GetMapping("/one/{gradeId}")
    public ResponseEntity<GradeResponseDTO> getGrade(@PathVariable("gradeId") Long gradeId){

        log.info("1) 최종 성적 단건 조회 요청 - gradeId-:{}",gradeId);

        GradeResponseDTO response= gradeService.getGrade(gradeId);

        log.info("Complete: 최종 성적 조회 완료 - gradeId-:{}, letterGrade-:{}",
                response.getGradeId(), response.getLetterGrade());

        return ResponseEntity.ok(response);
    }

    //NEW-G-3) 학생의 모든 성적을 조회하기 위해 컨트롤러 내부에 선언된 Crud(get)
    @GetMapping("/my/{email}")
    public ResponseEntity<List<GradeResponseDTO>> getMyGrade(
            @RequestHeader(value = "X-User-Email", required = false) String studentEmail){

        log.info("1) 학생의 본인 성적 조회 요청 - 학생-:{}", studentEmail);

        // Postman 테스트용
        if (studentEmail == null || studentEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            studentEmail = "hannah@aaa.com";
        }

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

    //NEW-G-5) 최종 성적을 수정 하기 위해 컨트롤러 내부에 선언된 Crud(get)
    @PutMapping("/update")
    public ResponseEntity<GradeResponseDTO> updateGrade(
            @Valid @RequestBody GradeUpdateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String professorEmail) {

        log.info("1) 최종 성적 수정 요청 (DTO 방식) - GradeId: {}, 교수: {}", dto.getGradeId(), professorEmail);

        // [Postman 테스트용]
        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            professorEmail = "hannah@aaa.com";
        }

        // Service 호출 시 DTO 안의 GradeId를 첫 번째 인자로 전달
        GradeResponseDTO response = gradeService.updateGrade(dto.getGradeId(), dto, professorEmail);

        log.info("Complete: 최종 성적 수정 완료 - GradeId: {}, LetterGrade: {}",
                response.getGradeId(), response.getLetterGrade());

        return ResponseEntity.ok(response);
    }

    //NEW-G-6) 최종 성적 자동 계산 및 저장하기 위해 컨트롤러 내부에 선언된 crud(post)
    @PostMapping("/enrollments/{enrollmentId}/calculate")
    public ResponseEntity<GradeResponseDTO> calculateAndSaveGrade(
            @PathVariable Long enrollmentId,
            @RequestHeader(value = "X-User-Email", required = false) String professorEmail) {

        log.info("1) 최종 성적 자동 계산 요청 - enrollmentId: {}, 교수: {}",
                enrollmentId, professorEmail);

        // Postman 테스트용
        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            professorEmail = "hannah@aaa.com";
        }

        GradeResponseDTO response = gradeService.calculateAndSaveGrade(enrollmentId, professorEmail);

        log.info("최종 성적 계산 완료 - gradeId: {}, totalScore: {}, letterGrade: {}",
                response.getGradeId(), response.getTotalScore(), response.getLetterGrade());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}


