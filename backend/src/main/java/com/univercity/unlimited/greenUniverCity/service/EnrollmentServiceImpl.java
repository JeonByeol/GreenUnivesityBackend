package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.DepartmentDTO;
import com.univercity.unlimited.greenUniverCity.dto.EnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.entity.Department;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.repository.DepartmentRepository;
import com.univercity.unlimited.greenUniverCity.repository.EnrollmentRepository;
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
public class EnrollmentServiceImpl implements EnrollmentService{
    private final EnrollmentRepository repository;
    private final ModelMapper mapper;


    @Override
    public List<EnrollmentDTO> findAllEnrollment() {
        List<EnrollmentDTO> dtoList=new ArrayList<>();
        for(Enrollment i:repository.findAll()){
            EnrollmentDTO r=mapper.map(i,EnrollmentDTO.class);
            dtoList.add(r);
        }
        return dtoList;
    }

    @Override
    public int addEnrollment(EnrollmentDTO enrollmentDTO) {
        log.info("1) 확인 : {}",enrollmentDTO);
        Enrollment enrollment = mapper.map(enrollmentDTO,Enrollment.class);
        log.info("확인 : {}",enrollment);
        try{
            repository.save(enrollment);
        } catch(Exception e) {
            return -1;
        }
        return 1;
    }
}
