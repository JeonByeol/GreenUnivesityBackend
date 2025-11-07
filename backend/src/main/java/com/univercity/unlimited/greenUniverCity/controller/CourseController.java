package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.dto.CourseDTO;
import com.univercity.unlimited.greenUniverCity.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/sibal")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/all")
    public List<CourseDTO> postmanTestCourse(){
        List<CourseDTO> list=courseService.findAllCourse();
        return list;
    }
}
