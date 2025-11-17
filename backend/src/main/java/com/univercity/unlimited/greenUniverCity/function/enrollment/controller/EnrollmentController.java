package com.univercity.unlimited.greenUniverCity.function.enrollment.controller;

import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/enrollment")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @GetMapping("/all")
    public List<EnrollmentDTO> postmanTestDepartment(){
        List<EnrollmentDTO> list=enrollmentService.findAllEnrollment();
        return list;
    }
}
