package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.dto.CourseDTO;
import com.univercity.unlimited.greenUniverCity.dto.GradeDTO;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import com.univercity.unlimited.greenUniverCity.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public List<GradeDTO> postmanMyGrade(@PathVariable("email") String email){
       return gradeService.findMyGrade(email);
    }

}
