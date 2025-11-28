package com.univercity.unlimited.greenUniverCity.function.department.service;

import com.univercity.unlimited.greenUniverCity.function.department.dto.DepartmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.department.dto.DepartmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.department.dto.DepartmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.department.dto.LegacyDepartmentDTO;
import com.univercity.unlimited.greenUniverCity.function.department.entity.Department;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    List<LegacyDepartmentDTO> regacyFindAllDepartment();

    List<DepartmentResponseDTO> findAllDepartment();
    List<DepartmentResponseDTO> findById(Long departmentId);
    DepartmentResponseDTO createDepartmentByAuthorizedUser(DepartmentCreateDTO dto, String email);
    DepartmentResponseDTO updateDepartmentByAuthorizedUser(DepartmentUpdateDTO dto, String email);
    Map<String,String> deleteByDepartmentId(Long departmentId, String email);

    Department findEntityById(Long departmentId);

    int addDepartment(LegacyDepartmentDTO legacyDepartmentDTO);
}
