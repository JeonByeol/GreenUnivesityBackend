package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.CourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.dto.GradeDTO;
import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import com.univercity.unlimited.greenUniverCity.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.repository.GradeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService{

    private final GradeRepository repository;

    private final EnrollmentRepository enrollmentRepository;

    private final CourseOfferingRepository courseOfferingRepository;

    private final ModelMapper mapper;

    @Override
    public List<GradeDTO> findAllGrade() {
        log.info("전체 성적 조회");
        List<GradeDTO> dto=new ArrayList<>();
        for(Grade i:repository.findAll()){
            GradeDTO r=mapper.map(i,GradeDTO.class);
            dto.add(r);
        }
        return dto;
    }

    @Override
    public ResponseEntity<String> addGrade(GradeDTO gradeDTO) {
        return null;
    }

    @Override
    public Optional<List<GradeDTO>> findAllGradeDTO() {
        List<Grade> grades = repository.findAll();
        List<GradeDTO> gradeDTOS = grades.stream().map(grade ->
                mapper.map(grade, GradeDTO.class)).toList();

        Optional<List<GradeDTO>> optionalGradeDTOS = Optional.of(gradeDTOS);
        return optionalGradeDTOS;
    }

    @Transactional
    @Override
    public List<GradeDTO> myGrade(String email) {
        List<Grade> grades= repository.findByMyGrade(email);
        List<GradeDTO> dto=new ArrayList<>();
        log.info("1) dto의 개수가 몇 개인가 :{}",dto);
        for(Grade i:grades){
            log.info("2) 여기는 어떻게 들어오는가 :{}",i);
            GradeDTO r=mapper.map(i,GradeDTO.class);
            log.info("3) r은 정상적인가 :{}",r);
            dto.add(r);
        }
        return dto;
    }


    @Override
    @Transactional
    public GradeDTO postNewGrade(String gradeValue) {
//        Optional<Enrollment> enrollment=enrollmentRepository.findById(enrollmentId);

        Enrollment enrollment=enrollmentRepository.findById(1l)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 수강신청 내역이 없습니다: " + 1l));

        List<CourseOffering> offering=courseOfferingRepository.findAll();
        if(enrollment.getEnrollmentId()==null)
            return null;

        List<CourseOfferingDTO> dto=new ArrayList<>();
        for(CourseOffering i:courseOfferingRepository.findAll()){
            CourseOfferingDTO r=mapper.map(i,CourseOfferingDTO.class);
            dto.add(r);
        }

        Optional<CourseOfferingDTO> bb = dto.stream().filter(i -> i.getCourseName().equals("자료구조A")).findAny();



        Grade newGrade = Grade.builder()
                .gradeValue(gradeValue)
                .build();

        Grade saveEntity=repository.save(newGrade);
        GradeDTO dto=mapper.map(saveEntity,GradeDTO.class);

        return dto;
    }



//    @Override
//    public List<GradeDTO> findMyGrade(String email) {
//        List<Grade> grades=repository.findByStudent(email);
//        log.info("1) 제발 들어와라 grades:{}",grades);
//
//        return grades.stream()
//                .map(grade -> mapper.map(grade, GradeDTO.class))
//                .collect(Collectors.toList());
//    }
}
