package com.univercity.unlimited.greenUniverCity.function.enrollment.service;

import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentTestDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
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


    @Override //todo E-1)
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

    @Override //todo E-2)
    public EnrollmentTestDTO getEnrollmentForGrade(Long id) {
        Enrollment e=repository.findByEnrollmentId(id);

        CourseOffering co=e.getCourseOffering();

        return EnrollmentTestDTO.builder()
                .enrollmentId(e.getEnrollmentId())
//                .studentId(e.getUser().getUserId())
                .offeringId(co.getOfferingId())
                .courseName(co.getCourse().getCourseName())
                .studentName(e.getUser().getNickname())
                .build();
    }
//        return mapper.map(enrollment,EnrollmentTestDTO.class);


    @Override //todo E-3)
    public Enrollment getEnrollmentEntity(Long id) {
        return repository.findByEnrollmentId(id);
    }


    @Override
    public List<EnrollmentDTO> getEnrollmentFindUser(Long id) {
        List<EnrollmentDTO> list=new ArrayList<>();
        return null;
    }
}
