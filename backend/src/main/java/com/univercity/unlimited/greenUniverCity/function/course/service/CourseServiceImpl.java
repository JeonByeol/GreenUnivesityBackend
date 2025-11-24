package com.univercity.unlimited.greenUniverCity.function.course.service;

import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseDTO;
import com.univercity.unlimited.greenUniverCity.function.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.course.repository.CourseRepository;
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

    @Override
    public int addCourse(CourseDTO courseDTO) {
        log.info("1) 확인 : {}",courseDTO);
        Course course = mapper.map(courseDTO,Course.class);
        log.info("확인 : {}",course);
        try{
            repository.save(course);
        } catch(Exception e) {
            return -1;
        }
        return 1;
    }

//    @Override//C-3) Timetable에 강의명을 넘겨주기 위해 구성한 serviceImpl
//    public CourseDTO findByCourseNameForTimeTable(Long id) {
//        Course c=repository.findByCourseId(id);
//        return CourseDTO.builder()
//                .courseName(c.getCourseName())
//                .build();
//    }
}
