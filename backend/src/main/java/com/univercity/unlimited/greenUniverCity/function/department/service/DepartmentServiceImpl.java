package com.univercity.unlimited.greenUniverCity.function.department.service;

import com.univercity.unlimited.greenUniverCity.function.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.department.repository.DepartmentRepository;
import com.univercity.unlimited.greenUniverCity.function.department.dto.LegacyDepartmentDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentRepository repository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public List<LegacyDepartmentDTO> findAllDepartment() {
        List<LegacyDepartmentDTO> dtoList=new ArrayList<>();
        for(Department i:repository.findAll()){
            LegacyDepartmentDTO r=mapper.map(i, LegacyDepartmentDTO.class);
            dtoList.add(r);
        }
        return dtoList;
    }

    @Override
    public int addDepartment(LegacyDepartmentDTO legacyDepartmentDTO) {
        log.info("1) 확인 : {}", legacyDepartmentDTO);
        Department department = mapper.map(legacyDepartmentDTO,Department.class);
        log.info("확인 : {}",department);
        try{
            repository.save(department);
        } catch(Exception e) {
            return -1;
        }
        return 1;
    }
}
