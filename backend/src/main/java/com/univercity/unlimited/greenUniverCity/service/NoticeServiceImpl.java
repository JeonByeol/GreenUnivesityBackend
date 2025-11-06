package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final CourseRepository courseRepository;
    @Override
    public List<Course> findAllCourse() {
        log.info("모든과정을 조회");
        return courseRepository.findAll();
    }

    @Override
    public List<Course> findPartCourse(String course_id, String course_name) {
       log.info("해당 강의를 조회");
       return  courseRepository.findAll();
    }
}
