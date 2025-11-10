package com.univercity.unlimited.greenUniverCity.controller;

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
        // 관리자가 모든 성적을 조회하기 위해 필요한 get방식 코드
    }

}
