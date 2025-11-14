package com.univercity.unlimited.greenUniverCity.function.timetable.service;

import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TimeTableService {
    List<TimeTableDTO> findAllTimeTable();

    ResponseEntity<String> addTimeTable(TimeTableDTO timeTableDTO);
}
