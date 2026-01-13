package com.univercity.unlimited.greenUniverCity.function.academic.course.service;

import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.member.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.member.department.service.DepartmentService;
import com.univercity.unlimited.greenUniverCity.util.BaseApplyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CourseApplyMapper
        extends BaseApplyMapper<CourseCreateDTO, CourseUpdateDTO, Course> {

    public CourseApplyMapper() {
        super("Course");
    }

    @Override
    protected void validateCreate(CourseCreateDTO dto) {
        super.validateCreate(dto);

        if (dto.getCourseName() == null || dto.getCourseName().isBlank()) {
            throw new IllegalArgumentException("courseName은 필수입니다.");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("description은 필수입니다.");
        }
        if (dto.getCredits() == null) {
            throw new IllegalArgumentException("credits는 필수입니다.");
        }
        if (dto.getDepartmentId() == null) {
            throw new IllegalArgumentException("departmentId는 필수입니다.");
        }
    }

    @Override
    protected void validateUpdate(CourseUpdateDTO dto) {
        super.validateUpdate(dto);

        if (dto.getCourseId() == null) {
            throw new IllegalArgumentException("courseId는 필수입니다.");
        }
        if (dto.getCourseName() == null || dto.getCourseName().isBlank()) {
            throw new IllegalArgumentException("courseName은 필수입니다.");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("description은 필수입니다.");
        }
        if (dto.getCredits() == null) {
            throw new IllegalArgumentException("credits는 필수입니다.");
        }
        if (dto.getDepartmentId() == null) {
            throw new IllegalArgumentException("departmentId는 필수입니다.");
        }
    }

    @Override
    protected void validateEntity(Course entity) {
        super.validateEntity(entity);

        if (entity.getCourseId() == null) {
            throw new IllegalArgumentException("Course가 영속화되지 않았습니다.");
        }
        if (entity.getCourseName() == null || entity.getCourseName().isBlank()) {
            throw new IllegalArgumentException("courseName이 비어 있습니다.");
        }
        if (entity.getCredits() == null) {
            throw new IllegalArgumentException("credits가 null입니다.");
        }
        if (entity.getDepartment() == null) {
            throw new IllegalArgumentException("department가 null입니다.");
        }
    }

    @Override
    protected Course createEntity(CourseCreateDTO dto) {
        return Course.builder()
                .courseName(dto.getCourseName())
                .description(dto.getDescription())
                .credits(dto.getCredits())
                .build();
    }

    @Override
    protected Course updateEntity(CourseUpdateDTO dto, Course entity) {
        entity.setCourseName(dto.getCourseName());
        entity.setDescription(dto.getDescription());
        entity.setCredits(dto.getCredits());

        return entity;
    }
}