package com.univercity.unlimited.greenUniverCity.function.offering.service;

import com.univercity.unlimited.greenUniverCity.function.offering.dto.CourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.offering.repository.CourseOfferingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseOfferingServiceImpl implements CourseOfferingService{
    private final CourseOfferingRepository repository;

    private final ModelMapper mapper;

    @Override
    public Optional<List<CourseOfferingDTO>> findAllCourseOfferingDTO() {
        List<CourseOffering> courseOfferings = repository.findAll();
        List<CourseOfferingDTO> courseOfferingDTOS = courseOfferings.stream().map(courseOffering ->
                mapper.map(courseOffering, CourseOfferingDTO.class)).toList();

        Optional<List<CourseOfferingDTO>> optionalCourseOfferingDTOS = Optional.of(courseOfferingDTOS);
        return optionalCourseOfferingDTOS;
    }

    @Override
    public int addCourseOffering(CourseOfferingDTO courseOfferingDTO) {
        log.info("1) 확인 : {}",courseOfferingDTO);
        CourseOffering courseOffering = mapper.map(courseOfferingDTO,CourseOffering.class);
        log.info("확인 : {}",courseOffering);
        try{
            repository.save(courseOffering);
        } catch(Exception e) {
            return -1;
        }
        return 1;
    }

}
