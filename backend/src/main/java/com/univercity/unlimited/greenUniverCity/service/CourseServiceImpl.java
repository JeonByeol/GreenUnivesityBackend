package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.CourseDTO;
import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.repository.CourseRepository;
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
public class CourseServiceImpl implements CourseService{
    private final CourseRepository repository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public List<CourseDTO> findAllCourse() {
        List<CourseDTO> dtoList=new ArrayList<>();
        for(Course i:repository.findAll()){
            CourseDTO r=mapper.map(i,CourseDTO.class);
            dtoList.add(r);
        }
        return dtoList;
    }
}
