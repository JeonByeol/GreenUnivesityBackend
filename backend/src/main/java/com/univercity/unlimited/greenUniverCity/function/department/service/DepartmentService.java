package com.univercity.unlimited.greenUniverCity.function.department.service;

import com.univercity.unlimited.greenUniverCity.function.department.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> findAllDepartment();

    int addDepartment(DepartmentDTO departmentDTO);
}
