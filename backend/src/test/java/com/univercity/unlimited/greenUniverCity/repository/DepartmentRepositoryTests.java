package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.entity.Department;
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
    DepartmentRepository repository;

    @Test
    @Tag("push")
    public void insertInitData() {
        // 데이터 세팅
        var departmentArray = List.of("컴퓨터","정보","전기");

        // 데이터 저장
        for(String departmentName : departmentArray) {
            Department department = Department.builder()
                    .deptName(departmentName)
                    .build();

            repository.save(department);
        }
    }

}
