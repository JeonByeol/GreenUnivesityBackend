package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.GradeDTO;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import com.univercity.unlimited.greenUniverCity.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService{
    private final GradeRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<GradeDTO> findAllGrade() {
        log.info("전체 성적 조회");
        List<GradeDTO> dto=new ArrayList<>();
        for(Grade i:repository.findAll()){
            GradeDTO r=mapper.map(i,GradeDTO.class);
            dto.add(r);
        }
        return dto;
    }
}
