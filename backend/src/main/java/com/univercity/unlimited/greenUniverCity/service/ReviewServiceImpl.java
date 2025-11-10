package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.EnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.dto.ReviewDTO;
import com.univercity.unlimited.greenUniverCity.entity.Review;
import com.univercity.unlimited.greenUniverCity.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository repository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public List<ReviewDTO> findAllReview() {
        int cnt=0;
        log.info("2) 여기는 전체 리뷰 조회 service입니다 service ");
        List<ReviewDTO> dtoList=new ArrayList<>();
        for(Review i:repository.findAll()){
            log.info("3 ) 여기는 전체 리뷰 조회 service입니다 service,{}  " ,i.getEnrollment());
//            EnrollmentDTO enrolmentDTO = mapper.map(i.getEnrollment(), EnrollmentDTO.class);
            EnrollmentDTO enrollmentDTO = mapper.map(i.getEnrollment(), EnrollmentDTO.class);
            log.info("4 ) 여기는 전체 리뷰 조회 service입니다 service,{}  " ,enrollmentDTO);
            ReviewDTO r=mapper.map(i,ReviewDTO.class);
//            r.setEnrollmentDTO(i.getEnrollment());
            r.setEnrollmentDTO(enrollmentDTO);
            log.info("5 ) cnt= {} 여기는 전체 리뷰 조회 service입니다 service,{}  " , cnt++ , i);
            dtoList.add(r);
        }
        return dtoList;
    }
}
