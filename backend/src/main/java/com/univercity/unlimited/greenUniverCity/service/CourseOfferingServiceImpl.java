package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.BoardDTO;
import com.univercity.unlimited.greenUniverCity.dto.CourseDTO;
import com.univercity.unlimited.greenUniverCity.dto.CourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.entity.Board;
import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.repository.BoardRepository;
import com.univercity.unlimited.greenUniverCity.repository.CourseOfferingRepository;
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
}
