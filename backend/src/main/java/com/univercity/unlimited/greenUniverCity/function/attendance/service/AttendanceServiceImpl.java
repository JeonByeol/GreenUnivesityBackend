package com.univercity.unlimited.greenUniverCity.function.attendance.service;

import com.univercity.unlimited.greenUniverCity.function.attendance.dto.AttendanceDTO;
import com.univercity.unlimited.greenUniverCity.function.attendance.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.attendance.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.function.enrollment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<Enrollment> findPartEnrollment(User user, Long enrollmentId) {
        log.info("해당 강의의 id와 유저의 정보를 조회");
        return  enrollmentRepository.findAll();

    }

    @Override
    public List<AttendanceDTO> findAllat() {
        List<AttendanceDTO> dto=new ArrayList<>();
        for(Attendance i:repository.findAll()){
            AttendanceDTO r=mapper.map(i,AttendanceDTO.class);
            dto.add(r);
        }
        return dto;
    }

//    @Override
//    public Optional<List<AttendanceDTO>> findAllAttendance() {
//        List<Attendance> attendances = repository.findAll();
//        List<AttendanceDTO> attendanceDTOS = attendances.stream().map(attendance ->
//                mapper.map(attendance, AttendanceDTO.class)).toList();
//
//        Optional<List<AttendanceDTO>> optionalAttendanceDTOS = Optional.of(attendanceDTOS);
//        return optionalAttendanceDTOS;
//    }

    @Override
    public ResponseEntity<String> addAttendance(AttendanceDTO attendanceDTO) {

        return null;
    }
}
