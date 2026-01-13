package com.univercity.unlimited.greenUniverCity.function.academic.course.service;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.entity.AcademicTerm;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.service.AcademicTermApplyMapper;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.service.AcademicTermValidationService;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.academic.course.repository.CourseRepository;
import com.univercity.unlimited.greenUniverCity.function.member.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.member.department.service.DepartmentService;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import com.univercity.unlimited.greenUniverCity.util.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{
    // Repository
    private final CourseRepository repository;

    // Entity -> ResponseDTO
    private final EntityMapper entityMapper;
    // CreateDTO, UpdateDTO -> Entity
    private final CourseApplyMapper applyMapper;
    // Validate ResponseDTO
    private final CourseValidationService validationService;

    // Common Name
    private final String baseName = "Course";

    // Service
    private final DepartmentService departmentService;

    private Course findOneEntityById(Long id) {
        log.info("1) {} 단건 조회 시작: id={}", baseName, id);
        Course entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(baseName + " not found with id: " + id));
        applyMapper.validateEntity(entity);
        log.info("2) {} 단건 조회 완료: {}", baseName, entity.getCourseId());
        return entity;
    }

    // ===============================
    // 전체 조회
    // ===============================
    @Override
    public List<CourseResponseDTO> findAllCourse() {
        log.info("{} 전체 조회 시작", baseName);

        List<Course> entityList = repository.findAll();
        List<CourseResponseDTO> responseDTOList = new ArrayList<>();

        for (Course entity : entityList) {
            try {
                CourseResponseDTO responseDTO = entityMapper.toCourseResponseDTO(entity);
                validationService.validateResponse(responseDTO);
                responseDTOList.add(responseDTO);
            } catch (Exception e) {
                log.error("{} 변환 실패: termId={}, semester={}, error={}",
                        baseName, entity.getCourseId(), entity.getCourseName(), e.getMessage(), e);
            }
        }

        log.info("{} 전체 조회 완료: 성공 {}건 / 전체 {}건",
                baseName, responseDTOList.size(), entityList.size());

        return responseDTOList;
    }

    // ===============================
    // 단건 조회
    // ===============================
    @Override
    public CourseResponseDTO findById(Long courseId) {
        Course entity = findOneEntityById(courseId);

        // 변환 + 검증
        CourseResponseDTO responseDTO = entityMapper.toCourseResponseDTO(entity);
        validationService.validateResponse(responseDTO);

        log.info("{} 조회 완료: termId={}", baseName, courseId);
        return responseDTO;
    }

    @Override
    public CourseResponseDTO createCourse(CourseCreateDTO dto) {
        log.info("{} 생성 시작: {}", baseName, dto);

        // DTO -> Entity (검증 포함)
        Course entity = applyMapper.applyCreate(dto);

        // =========================
        // department 세팅
        // =========================
        if (dto.getDepartmentId() != null) {
            Department department = departmentService.findEntityById(dto.getDepartmentId());
            entity.setDepartment(department);
        }

        Course savedEntity = repository.save(entity);
        log.info("{} 생성 완료: {}", baseName, savedEntity.getCourseId());

        // Entity -> ResponseDTO + 검증
        CourseResponseDTO responseDTO = entityMapper.toCourseResponseDTO(savedEntity);
        validationService.validateResponse(responseDTO);

        return responseDTO;
    }

    // ===============================
    // 수정
    // ===============================
    @Override
    @Transactional
    public CourseResponseDTO updateCourse(CourseUpdateDTO dto) {
        log.info("{} 수정 시작: termId={}", baseName, dto.getCourseId());

        // 단건 조회
        Course entity = findOneEntityById(dto.getCourseId());

        // =========================
        // department 세팅
        // =========================
        if (dto.getDepartmentId() != null) {
            Department department = departmentService.findEntityById(dto.getDepartmentId());
            entity.setDepartment(department);
        }

        // DTO -> Entity 업데이트 (검증 포함)
        Course updatedEntity = applyMapper.applyUpdate(dto, entity);

        Course savedEntity = repository.save(updatedEntity);
        log.info("{} 수정 완료: Id = {}", baseName, savedEntity.getCourseId());

        // Entity -> ResponseDTO + 검증
        CourseResponseDTO responseDTO = entityMapper.toCourseResponseDTO(savedEntity);
        validationService.validateResponse(responseDTO);

        return responseDTO;
    }

    // ===============================
    // 삭제
    // ===============================
    @Override
    public Map<String,String> deleteCourse(Long courseId) {
        log.info("{} 삭제 시작: Id = {}", baseName, courseId);

        Course entity = findOneEntityById(courseId);
        repository.delete(entity);

        log.info("{} 삭제 완료: Id = {}", baseName, courseId);
        return Map.of("Result", "Success");
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
