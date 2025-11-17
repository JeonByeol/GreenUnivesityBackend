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

    @GetMapping("/all")
    public List<GradeDTO> postmanTestGrade(){
        log.info("Controller: 성적전체조회");
        return gradeService.findAllGrade();
    }

    @GetMapping("/mygrade/{email}") // 로그인 한 학생의 정보에 맞는 성적을 뽑아서 쓸 수 있게 만든코드
    public List<GradeStudentDTO> postmanMyGrade(@PathVariable("email") String email){
       return gradeService.myGrade(email);
    }

    @GetMapping("/course/{offeringId}")
    public List<GradeProfessorDTO> postmanCourseGrade(@PathVariable("offeringId") Long offeringId){
        return gradeService.courseOfGrade(offeringId);
    }

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
