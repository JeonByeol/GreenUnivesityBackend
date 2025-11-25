package com.univercity.unlimited.greenUniverCity.function.course.controller;

import com.univercity.unlimited.greenUniverCity.function.course.dto.LegacyCourseDTO;
import com.univercity.unlimited.greenUniverCity.function.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/all")
    public List<LegacyCourseDTO> postmanTestCourse(){
        return courseService.findAllCourse();
    }
}
