package com.univercity.unlimited.greenUniverCity.function.timetable.service;

import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableResponseDTO;
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

    private final CourseOfferingService offeringService;

    private final ModelMapper mapper;

    /**
     * T-A) TimeTable 엔티티를 (Response)DTO로 변환 | 각각의 crud 기능에 사용되는 서비스 구현부에 사용하기 위해 함수로 생성
     */
    private TimeTableResponseDTO toResponseDTO(TimeTable timeTable){
        CourseOffering courseOffering=timeTable.getCourseOffering();


        return
                TimeTableResponseDTO.builder()
                        .timetableId(timeTable.getTimetableId())
                        .startTime(timeTable.getStartTime())
                        .endTime(timeTable.getEndTime())
                        .location(timeTable.getLocation())
                        .dayOfWeek(timeTable.getDayOfWeek())
                        .courseName(courseOffering.getCourseName())

                        .build();
    }

    //T-1) 리뷰 테이블에 존재하는 모든 데이터를 조회하기 위한 서비스 구현부
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

    //T-2)특정 과목에 존재하는 모든 시간표를 조회하기 위한 서비스 구현부
    @Override
    public List<TimeTableResponseDTO> get2(String dayOfWeek) {
        List<TimeTable> timeTables=repository.findByDayTimeTable(dayOfWeek);

        return timeTables.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    //T-3)특정 학생이 신청한 모든 과목의 시간표를 조회하기 위한 서비스 구현부
    @Override
    public List<TimeTableResponseDTO> get(String email) {
        List<TimeTable> timeTables = repository.findTimetableByStudentEmail(email);

        return timeTables.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    //T-4) 교수 or 관리자가 특정 강의에 대한 시간표를 생성하기 위한 서비스 구현부
    // -> 시간표에 대한 생성 기능은 구현 o 하지만 시간표를 생성할떄 그 강의
    // 구상: serviceImpl 구현부내에 userId값을 받아서 검증하는 함수를 만들어서 검증예정
    @Override
    public TimeTableResponseDTO post(TimeTableCreateDTO dto, String requesterEmail) {
        log.info("2) 시간표 생성 -교수:{}, offeringId:{}",requesterEmail,dto.getOfferingId());

        CourseOffering offering=offeringService.getCourseOfferingEntity(dto.getOfferingId());

        TimeTable timeTable=TimeTable.builder()
                .courseOffering(offering)
                .dayOfWeek(dto.getDayOfWeek())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .location(dto.getLocation())
                .build();

        TimeTable saveTimeTable=repository.save(timeTable);

        log.info("3) 시간표 생성 완료 -timetableId:{}, 교수:{}",saveTimeTable.getTimetableId(),requesterEmail);

        return toResponseDTO(saveTimeTable);
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
