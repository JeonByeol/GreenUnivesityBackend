package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.function.department.repository.DepartmentRepository;
import com.univercity.unlimited.greenUniverCity.function.department.dto.DepartmentDTO;
import com.univercity.unlimited.greenUniverCity.function.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.department.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class DepartmentRepositoryTests {
    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private DepartmentService service;

    @Test
    @Tag("push")
    public void insertInitData() {
        // 데이터 세팅
        var departmentArray = List.of("컴퓨터","정보","전기");

        // 데이터 저장a
        for(String departmentName : departmentArray) {
            Department department = Department.builder()
                    .deptName(departmentName)
                    .build();

            repository.save(department);
        }
    }

    @Test
    public void insertDepartmentData() {
        String[] departmentArray = {
                "기계",
                "화학",
                "생명과학",
                "환경공학",
                "건축",
                "산업디자인",
                "경영",
                "경제",
                "심리학",
                "체육"
        };
        String department = departmentArray[(int)(Math.random()*departmentArray.length)];
        DepartmentDTO departmentDTO = DepartmentDTO.builder()
                .deptName(department)
                .build();

        service.addDepartment(departmentDTO);
    }

}
