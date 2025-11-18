package com.univercity.unlimited.greenUniverCity.function.timetable.service;

import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseDTO;
import com.univercity.unlimited.greenUniverCity.function.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.course.service.CourseService;
import com.univercity.unlimited.greenUniverCity.function.offering.dto.CourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableStudentDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.function.timetable.repository.TimeTableRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeTableServiceImpl implements TimeTableService{
    private final TimeTableRepository repository;
    private final ModelMapper mapper;
    private final CourseService courseService;

    @Transactional
    @Override
    public List<TimeTableDTO> findAllTimeTable() {
        log.info("2) 여기는 시간표 전체조회 서비스입니다");
        List<TimeTableDTO> dto=new ArrayList<>();
        for(TimeTable i:repository.findAll()){
            TimeTableDTO r= mapper.map(i,TimeTableDTO.class);
            dto.add(r);
        }
        return dto;
    }

    @Override //T-2)특정 과목에 대한 시간표 조회
    public List<TimeTableStudentDTO> get2(String dayOfWeek) {
        List<TimeTable> timeTables=repository.findByDayTimeTable(dayOfWeek);

        return timeTables.stream()
                .map(t-> {
                    CourseOffering info=t.getCourseOffering();
//                      CourseDTO info=
//                              courseService.findByCourseNameForTimeTable(t.getCourseOffering().getCourse().getCourseId());
                    return
                    TimeTableStudentDTO.builder()
                            .timetableId(t.getTimetableId())
                            .startTime(t.getStartTime())
                            .endTime(t.getEndTime())
                            .location(t.getLocation())
                            .dayOfWeek(t.getDayOfWeek())
                            .courseName(info.getCourseName())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override//T-3)특정 학생이 신청한 모든 과목의 시간표를 조회
    public List<TimeTableStudentDTO> get(String email) {
        List<TimeTable> timeTables = repository.findTimetableByStudentEmail(email);
        return timeTables.stream()
                .map(t -> {
                    CourseOffering info=t.getCourseOffering();
//                    CourseDTO info=
//                            courseService.findByCourseNameForTimeTable(t.getCourseOffering().getCourse().getCourseId());
                    return
                    TimeTableStudentDTO.builder()
                            .timetableId(t.getTimetableId())
                            .startTime(t.getStartTime())
                            .endTime(t.getEndTime())
                            .location(t.getLocation())
                            .dayOfWeek(t.getDayOfWeek())
                            .courseName(info.getCourseName())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public TimeTable post() {
        return null;
    }

    @Override
    public TimeTable put() {
        return null;
    }

    @Override
    public TimeTable delete() {
        return null;
    }

    @Override
    public ResponseEntity<String> addTimeTable(TimeTableDTO timeTableDTO) {
        return null;
    }
}
