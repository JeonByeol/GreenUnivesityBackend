package com.univercity.unlimited.greenUniverCity.function.department.service;

import com.univercity.unlimited.greenUniverCity.function.department.dto.DepartmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.department.dto.DepartmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.department.dto.DepartmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.department.repository.DepartmentRepository;
import com.univercity.unlimited.greenUniverCity.function.department.dto.LegacyDepartmentDTO;
import com.univercity.unlimited.greenUniverCity.util.MapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentRepository repository;
    private final ModelMapper mapper;

    @Transactional
    private DepartmentResponseDTO toResponseDTO(Department department){
        return
                DepartmentResponseDTO.builder()
                        .departmentId(department.getDepartmentId())
                        .deptName(department.getDeptName())
//                        .courseId(department.getCourses())
                        .build();
    }

    @Override
    public List<LegacyDepartmentDTO> regacyFindAllDepartment() {
        List<LegacyDepartmentDTO> dtoList=new ArrayList<>();
        for(Department i:repository.findAll()){
            LegacyDepartmentDTO r=mapper.map(i, LegacyDepartmentDTO.class);
            dtoList.add(r);
        }
        return dtoList;
    }

    @Transactional
    @Override
    public List<DepartmentResponseDTO> findAllDepartment() {
        log.info("2) Department 전체조회 시작");
        List<Department> departments = repository.findAll();

        log.info("3) Department 전체조회 성공");

        return departments.stream()
                .map(this::toResponseDTO).toList();
    }

    @Override
    public List<DepartmentResponseDTO> findById(Long departmentId) {
        log.info("2) Department 한개조회 시작 , departmentId : {}",departmentId);
        Optional<Department> department = repository.findById(departmentId);

        if(department.isEmpty()){
            throw new RuntimeException("Department not found with id: " + departmentId);
        }

        DepartmentResponseDTO responseDTO = toResponseDTO(department.get());
        return List.of(responseDTO);
    }

    @Override
    public DepartmentResponseDTO createDepartmentByAuthorizedUser(DepartmentCreateDTO dto, String email) {
        log.info("2)Department 추가 시작 Department : {}", dto);

        Department department = new Department();
        MapperUtil.updateFrom(dto, department,new ArrayList<>());
        log.info("3)DepartmentCreateDTO -> Department : {}", department);

        Department result = repository.save(department);
        log.info("4)추가된 Course : {}", result);

        return toResponseDTO(result);
    }

    @Override
    public DepartmentResponseDTO updateDepartmentByAuthorizedUser(DepartmentUpdateDTO dto, String email) {
        log.info("2)Department 수정 시작 Department : {}", dto);

        Optional<Department> departmentOptional = repository.findById(dto.getDepartmentId());

        if(departmentOptional.isEmpty()){
            throw new RuntimeException("Department not found with id: " + dto.getDepartmentId());
        }

        // 조회
        Department department = departmentOptional.get();

        log.info("3) 수정 이전 Department : {}",department);
        MapperUtil.updateFrom(dto,department,List.of("departmentId"));

        log.info("5) 기존 Department : {}",department);
        Department updateDepartment=repository.save(department);

        log.info("6) Department 수정 성공 updateDepartment:  {}",updateDepartment);

        return toResponseDTO(updateDepartment);
    }

    @Override
    public Map<String,String> deleteByDepartmentId(Long departmentId, String email) {
        log.info("2) Department 한개삭제 시작 , departmentId : {}",departmentId);
        Optional<Department> department = repository.findById(departmentId);

        if(department.isEmpty()){
            return Map.of("Result","Failure");
        }

        repository.delete(department.get());

        return Map.of("Result","Success");
    }

    @Override
    public Department findEntityById(Long departmentId) {
        Optional<Department> department = repository.findById(departmentId);
        if(department.isEmpty()) {
            throw new RuntimeException("");
        }

        return department.get();
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
