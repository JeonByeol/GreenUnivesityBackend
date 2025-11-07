package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.TimeTableDTO;
import com.univercity.unlimited.greenUniverCity.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.repository.TimeTableRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeTableServiceImpl implements TimeTableService{
    private final TimeTableRepository repository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public List<TimeTableDTO> findAllTimeTable() {
        log.info("여기는 시간표 전체조회 서비스입니다");
        List<TimeTableDTO> dto=new ArrayList<>();
        for(TimeTable i:repository.findAll()){
            TimeTableDTO r= mapper.map(i,TimeTableDTO.class);
            dto.add(r);
        }
        return dto;
    }
}
