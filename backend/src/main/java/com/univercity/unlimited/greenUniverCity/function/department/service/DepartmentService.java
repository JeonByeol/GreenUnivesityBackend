package com.univercity.unlimited.greenUniverCity.function.department.service;

import com.univercity.unlimited.greenUniverCity.function.department.dto.LegacyDepartmentDTO;

import java.util.List;

public interface DepartmentService {
    List<LegacyDepartmentDTO> findAllDepartment();

    int addDepartment(LegacyDepartmentDTO legacyDepartmentDTO);
}
