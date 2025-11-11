package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.dto.CourseDTO;
import com.univercity.unlimited.greenUniverCity.dto.DepartmentDTO;
import com.univercity.unlimited.greenUniverCity.entity.Department;
import com.univercity.unlimited.greenUniverCity.service.CourseService;
import com.univercity.unlimited.greenUniverCity.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/all")
    public List<DepartmentDTO> postmanTestDepartment(){
        List<DepartmentDTO> list=departmentService.findAllDepartment();
        return list;
    }
}
