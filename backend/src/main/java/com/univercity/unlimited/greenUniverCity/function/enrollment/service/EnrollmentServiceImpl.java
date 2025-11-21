package com.univercity.unlimited.greenUniverCity.function.enrollment.service;

import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.enrollment.exception.EnrollmentNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.enrollment.exception.UserNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.offering.exception.CourseOfferingNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository repository;

    private final ModelMapper mapper;

    //E-1)Enroll에 존재하는 모든 데이터 조회 서비스 구현부
    @Override
    public List<EnrollmentDTO> findAllEnrollment() {
        List<EnrollmentDTO> dtoList = new ArrayList<>();
        for (Enrollment i : repository.findAll()) {
            EnrollmentDTO r = mapper.map(i, EnrollmentDTO.class);
            dtoList.add(r);
        }
        return dtoList;
    }
    
    //E-2) **(기능 입력 바랍니다/사용 안할시 삭제 부탁드립니다)**
    @Override
    public int addEnrollment(EnrollmentDTO enrollmentDTO) {
        log.info("1) 확인 : {}", enrollmentDTO);
        Enrollment enrollment = mapper.map(enrollmentDTO, Enrollment.class);
        log.info("확인 : {}", enrollment);
        try {
            repository.save(enrollment);
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    //E-3)다른 service에서 enrollment와 여기에 속한 상위 테이블의 정보를 실질적으로 사용하기 위한 service 구현부
    //현재 사용위치: reviewServiceImpl에서 [ R-3) - 리뷰 생성 테이블에서 사용 ]
    @Override
    public Enrollment getEnrollmentEntity(Long id) {
        Enrollment enrollment = repository.findByEnrollmentId(id);

        //Enrollment 수강 내역 id에 대한 검증
        if (enrollment == null) {
            throw new EnrollmentNotFoundException(
                    "3) 보안 검사 시도 식별코드 -:E-3" +
                            "수강 정보를 찾을 수 없습니다. id: " + id);
        }

        //User 검증 추가 -- 현재 사용 안함
        if (enrollment.getUser() == null) {
            throw new UserNotFoundException("수강 정보에 연결된 사용자가 존재하지 않습니다 id:."+id);
        }

        //Offering 개설 강의 Id에 대한 검증
        if (enrollment.getCourseOffering() == null) {
            throw new CourseOfferingNotFoundException( "데이터 오류: 수강 정보에 개설 강의가 없습니다. id: " + id);
        }

        return enrollment;
    }
}

/**
 * E-4) 다른 클래스에 정보를 전달할 때 repository가 옳은지 service가 옳은지 검증 후 추후 사용 혹은 삭제 예정
 */
//@Override
//    public EnrollmentTestDTO getEnrollmentForGrade(Long id) {
//        Enrollment e=repository.findByEnrollmentId(id);
//
//        CourseOffering co=e.getCourseOffering();
//
//        return EnrollmentTestDTO.builder()
//                .enrollmentId(e.getEnrollmentId())
////                .studentId(e.getUser().getUserId())
//                .offeringId(co.getOfferingId())
//                .courseName(co.getCourse().getCourseName())
//                .studentName(e.getUser().getNickname())
//                .build();
//    }


/**
 * /E-5) 다른 클래스에 정보를 전달할 때 repository가 옳은지 service가 옳은지 검증 후 추후 사용 혹은 삭제 예정
 */
//    @Override
//    public List<EnrollmentDTO> getEnrollmentFindUser(Long id) {
//        List<EnrollmentDTO> list=new ArrayList<>();
//        return null;
//    }

