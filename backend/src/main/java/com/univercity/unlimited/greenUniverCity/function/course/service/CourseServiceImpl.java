package com.univercity.unlimited.greenUniverCity.function.course.service;

import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.course.dto.LegacyCourseDTO;
import com.univercity.unlimited.greenUniverCity.function.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.course.repository.CourseRepository;
import com.univercity.unlimited.greenUniverCity.function.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.department.service.DepartmentService;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.review.entity.Review;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.function.util.MapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{
    private final CourseRepository repository;

    private final DepartmentService departmentService;

    private final ModelMapper mapper;

    private CourseResponseDTO toResponseDTO(Course course){
        Department department = course.getDepartment();

        return
                CourseResponseDTO.builder()
                        .courseId(course.getCourseId())
                        .courseName(course.getCourseName())
                        .description(course.getDescription())
                        .credits(course.getCredits())
                        .departmentId(department != null ? department.getDepartmentId() : -1)
                        .build();
    }


    @Override
    public List<LegacyCourseDTO> legacyFindAllCourse() {
        List<LegacyCourseDTO> dtoList=new ArrayList<>();
        for(Course i:repository.findAll()){
            LegacyCourseDTO r=mapper.map(i, LegacyCourseDTO.class);
            dtoList.add(r);
        }
        return dtoList;
    }

    @Override
    public List<CourseResponseDTO> findAllCourse() {
        log.info("2) Course 전체조회 시작");
        List<Course> courses = repository.findAll();

        log.info("3) Course 전체조회 성공");

        return courses.stream()
                .map(this::toResponseDTO).toList();
    }


    @Override
    public List<CourseResponseDTO> findById(Long courseId) {
        log.info("2) Course 한개조회 시작 , courseId : {}",courseId);
        Optional<Course> course = repository.findById(courseId);

        if(course.isEmpty()){
            throw new RuntimeException("Course not found with id: " + courseId);
        }

        CourseResponseDTO responseDTO = toResponseDTO(course.get());
        return List.of(responseDTO);
    }

    @Override
    public CourseResponseDTO createCourseByAuthorizedUser(CourseCreateDTO dto, String email) {
        log.info("2)Course 추가 시작 course : {}", dto);

        Course course = new Course();
        MapperUtil.updateFrom(dto, course, List.of("departmentId"));
        log.info("3)CourseCreateDTO -> Course : {}", course);

        Department department = departmentService.findEntityById(dto.getDepartmentId());
        course.setDepartment(department);

        log.info("4)Department를 추가한 이후 Course : {}", course);

        Course result = repository.save(course);
        log.info("4)추가된 Course : {}", result);

        return toResponseDTO(result);
    }

    @Override
    public CourseResponseDTO updateCourseByAuthorizedUser(CourseUpdateDTO dto, String email) {
        log.info("2)Course 수정 시작 course : {}", dto);

        Optional<Course> courseOptional = repository.findById(dto.getCourseId());

        if(courseOptional.isEmpty()){
            throw new RuntimeException("Course not found with id: " + dto.getCourseId());
        }

        // 조회
        Course course = courseOptional.get();

        log.info("3) 수정 이전 Course : {}",course);
        MapperUtil.updateFrom(dto,course,List.of("courseId"));


        log.info("5) 기존 Course : {}",course);
        Course updateCourse=repository.save(course);

        log.info("6) Course 수정 성공 updateCourse:  {}",updateCourse);

        return toResponseDTO(updateCourse);
    }

    @Override
    public Map<String,String> deleteByCourseId(Long courseId, String email) {
        log.info("2) Course 한개삭제 시작 , courseId : {}",courseId);
        Optional<Course> course = repository.findById(courseId);

        if(course.isEmpty()){
            return Map.of("Result","Failure");
        }

        // 검증

        repository.delete(course.get());

        return Map.of("Result","Success");
    }

    @Override
    public Course getByCourseId(Long courseId) {
        Optional<Course> courseOptional = repository.findById(courseId);

        if(courseOptional.isEmpty()){
            throw new RuntimeException("Course를 찾지 못하였습니다.");
        }

        return courseOptional.get();
    }
}
