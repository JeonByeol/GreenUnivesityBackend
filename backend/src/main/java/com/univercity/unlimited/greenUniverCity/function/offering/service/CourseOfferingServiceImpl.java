package com.univercity.unlimited.greenUniverCity.function.offering.service;

import com.univercity.unlimited.greenUniverCity.function.offering.dto.CourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.offering.exception.CourseOfferingNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.offering.repository.CourseOfferingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseOfferingServiceImpl implements CourseOfferingService{
    private final CourseOfferingRepository repository;

    private final ModelMapper mapper;

    @Override
    public Optional<List<CourseOfferingDTO>> findAllCourseOfferingDTO() {
        List<CourseOffering> courseOfferings = repository.findAll();
        List<CourseOfferingDTO> courseOfferingDTOS = courseOfferings.stream().map(courseOffering ->
                mapper.map(courseOffering, CourseOfferingDTO.class)).toList();

        Optional<List<CourseOfferingDTO>> optionalCourseOfferingDTOS = Optional.of(courseOfferingDTOS);
        return optionalCourseOfferingDTOS;
    }

    @Override
    public int addCourseOffering(CourseOfferingDTO courseOfferingDTO) {
        log.info("1) 확인 : {}",courseOfferingDTO);
        CourseOffering courseOffering = mapper.map(courseOfferingDTO,CourseOffering.class);
        log.info("확인 : {}",courseOffering);
        try{
            repository.save(courseOffering);
        } catch(Exception e) {
            return -1;
        }
        return 1;
    }

    //CO-3)다른 service에서 CourseOffering과 여기에 속한 상위 테이블의 정보를 실질적으로 사용하기 위한 service 구현부
    //현재 사용위치: TimeTableServiceImpl에서 [ T-4/T-5 시간표 생성/수정 구현부에서 사용중]
    @Override
    public CourseOffering getCourseOfferingEntity(Long id) {
        CourseOffering offering= repository.findByOfferingId(id);

        //Offering 개설 강의 Id에 대한 검증
        if (offering == null) {
            throw new CourseOfferingNotFoundException(
                    "3) 보안 검사 시도 식별코드: CO-3)" +
                            "데이터 오류:개설 강의를 찾을 수 없습니다. id: " + id);
        }

        return offering;
    }

}
