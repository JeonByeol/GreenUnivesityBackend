package com.univercity.unlimited.greenUniverCity.function.grade.controller;

import com.univercity.unlimited.greenUniverCity.function.grade.dto.GradeDTO;
import com.univercity.unlimited.greenUniverCity.function.grade.dto.GradeProfessorDTO;
import com.univercity.unlimited.greenUniverCity.function.grade.service.GradeService;
import com.univercity.unlimited.greenUniverCity.function.grade.dto.GradeStudentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<GradeDTO> postmanTestGrade(){
        log.info("Controller: 성적전체조회");
        return gradeService.findAllGrade();
    }

    //G-2) 특정 학생이 본인이 수강한 모든 과목의 성적과 과목명을 조회
    @GetMapping("/mygrade/{email}")
    public List<GradeStudentDTO> postmanMyGrade(@PathVariable("email") String email){
       return gradeService.myGrade(email);
    }

    //G-3) 교수가 특정 과목의 수업을 듣는 전체학생 조회
    @GetMapping("/course/{offeringId}")
    public List<GradeProfessorDTO> postmanCourseGrade(@PathVariable("offeringId") Long offeringId){
        return gradeService.courseOfGrade(offeringId);
    }

    //G-4) 교수가 특정 학생에 대한 성적을 수정(입력)
    @PutMapping("{enrollmentId}")
    public ResponseEntity<GradeDTO> updateGrade(
            @PathVariable("enrollmentId") Long enrollmentId,
            @RequestBody GradeDTO gradeDTO) {

        String gradeValue=gradeDTO.getGradeValue();

        log.info(" 수강신청 ID[{}]의 성적을 [{}]로 입력 시도...",  enrollmentId, gradeValue);

        GradeDTO updateGrade=gradeService.postNewGrade(
                enrollmentId,
                gradeValue
        );
        return ResponseEntity.ok(updateGrade);
    }






//    @PostMapping("/test")
//    public GradeDTO postmanTestAdd(@RequestBody GradeDTO dto){
//        Long enrollment=dto.getEnrollment().getEnrollmentId();
//        String value=dto.getGradeValue();
//        String mail=dto.getEnrollment().getUser().getEmail();
//        return gradeService.postNewGrade(enrollment,value,mail);
//    }
}
