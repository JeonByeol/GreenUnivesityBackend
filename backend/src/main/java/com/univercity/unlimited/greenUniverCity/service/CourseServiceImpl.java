package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.CommentDTO;
import com.univercity.unlimited.greenUniverCity.dto.CourseDTO;
import com.univercity.unlimited.greenUniverCity.entity.Comment;
import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<String> addCourse(CourseDTO courseDTO) {
        return null;
    }

    @Override
    public Optional<List<CourseDTO>> findAllCourseDTO() {
        List<Course> courses = repository.findAll();
        List<CourseDTO> courseDTOS = courses.stream().map(course ->
                mapper.map(course, CourseDTO.class)).toList();

        Optional<List<CourseDTO>> optionalCommentDTOS = Optional.of(courseDTOS);
        return optionalCommentDTOS;
    }
}
