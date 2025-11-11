package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.TimeTableDTO;
import com.univercity.unlimited.greenUniverCity.entity.TimeTable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TimeTableService {
    List<TimeTableDTO> findAllTimeTable();

    ResponseEntity<String> addTimeTable(TimeTableDTO timeTableDTO);
}
