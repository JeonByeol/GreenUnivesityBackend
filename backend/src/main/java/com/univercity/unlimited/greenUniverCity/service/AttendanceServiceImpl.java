package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.AttendanceDTO;
import com.univercity.unlimited.greenUniverCity.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import com.univercity.unlimited.greenUniverCity.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository repository;

    private final ModelMapper mapper;

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

    @Override
    public Optional<List<AttendanceDTO>> findAllAttendance() {
        List<Attendance> attendances = repository.findAll();
        List<AttendanceDTO> attendanceDTOS = attendances.stream().map(attendance ->
                mapper.map(attendance, AttendanceDTO.class)).toList();

        Optional<List<AttendanceDTO>> optionalAttendanceDTOS = Optional.of(attendanceDTOS);
        return optionalAttendanceDTOS;
    }

    @Override
    public ResponseEntity<String> addAttendance(AttendanceDTO attendanceDTO) {

        return null;
    }
}
