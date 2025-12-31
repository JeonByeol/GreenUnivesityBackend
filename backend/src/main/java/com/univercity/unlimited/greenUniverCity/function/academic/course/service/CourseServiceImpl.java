package com.univercity.unlimited.greenUniverCity.function.academic.course.service;

import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.academic.course.repository.CourseRepository;
import com.univercity.unlimited.greenUniverCity.function.member.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.member.department.service.DepartmentService;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import com.univercity.unlimited.greenUniverCity.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{
    private final CourseRepository repository;

    private final DepartmentService departmentService;

    private final EntityMapper entityMapper;

    private final ModelMapper mapper;

    private Course toEntity(CourseUpdateDTO dto) {
        if (dto == null) return null;

        Course course = Course.builder()
                .courseId(dto.getCourseId())
                .courseName(dto.getCourseName())
                .description(dto.getDescription())
                .credits(dto.getCredits())
                .build();

        // ✅ department만 있을 때만 변경
        if (dto.getDepartmentId() != null) {
            Department department = departmentService.findEntityById(dto.getDepartmentId());
            course.setDepartment(department);
        }

        return course;
    }

    private Course toEntity(CourseCreateDTO dto) {
        if (dto == null) return null;

        Course course = Course.builder()
                .courseName(dto.getCourseName())
                .description(dto.getDescription())
                .credits(dto.getCredits())
                .build();

        // departmentId가 있으면 Department 세팅
        if (dto.getDepartmentId() != null) {
            Department department = departmentService.findEntityById(dto.getDepartmentId());
            course.setDepartment(department);
        }
        return course;
    }



    @Override
    public List<CourseResponseDTO> legacyFindAllCourse() {
        List<CourseResponseDTO> dtoList=new ArrayList<>();
        for(Course i:repository.findAll()){
            CourseResponseDTO r=mapper.map(i, CourseResponseDTO.class);
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
                .map(entityMapper::toCourseResponseDTO).toList();
    }


    @Override
    public List<CourseResponseDTO> findById(Long courseId) {
        log.info("2) Course 한개조회 시작 , courseId : {}",courseId);
        Optional<Course> course = repository.findById(courseId);

        if(course.isEmpty()){
            throw new RuntimeException("Course not found with id: " + courseId);
        }

        CourseResponseDTO responseDTO = entityMapper.toCourseResponseDTO(course.get());
        return List.of(responseDTO);
    }

    @Override
    public CourseResponseDTO createCourseByAuthorizedUser(CourseCreateDTO dto, String email) {
        log.info("2)Course 추가 시작 course : {}", dto);

        Course course = toEntity(dto);
//        log.info("3)CourseCreateDTO -> Course : {}", course);

        log.info("4)Department를 추가한 이후 Course : {}", course);

        Course result = repository.save(course);
        log.info("4)추가된 Course : {}", result);

        return entityMapper.toCourseResponseDTO(result);
    }

    @Override
    public CourseResponseDTO updateCourseByAuthorizedUser(CourseUpdateDTO dto, String email) {
        log.info("2)Course 수정 시작 course : {}", dto);

        Optional<Course> courseOptional = repository.findById(dto.getCourseId());

        if(courseOptional.isEmpty()){
            throw new RuntimeException("Course not found with id: " + dto.getCourseId());
        }

        // 조회

//        log.info("3) 수정 이전 Course : {}",course);
        Course course = toEntity(dto);

        log.info("5) 기존 Course : {}",course);
        Course updateCourse=repository.save(course);

        log.info("6) Course 수정 성공 updateCourse:  {}",updateCourse);

        return entityMapper.toCourseResponseDTO(updateCourse);
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
