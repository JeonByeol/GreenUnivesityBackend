package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import com.univercity.unlimited.greenUniverCity.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{

    private final EnrollmentRepository enrollmentRepository;
    @Override
    public List<Enrollment> findAllEnrollment() {
        log.info("모든 강의를 조회");
        return enrollmentRepository.findAll();
    }

    @Override
    public List<Enrollment> findPartEnrollment(UserVo userVo, Long enrollmentId) {
        log.info("해당 강의의 id와 유저의 정보를 조회");
        return  enrollmentRepository.findAll();

    }
}
