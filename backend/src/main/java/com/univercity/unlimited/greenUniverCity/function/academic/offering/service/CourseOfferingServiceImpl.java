package com.univercity.unlimited.greenUniverCity.function.academic.offering.service;

import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.academic.course.service.CourseService;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.exception.CourseOfferingNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseOfferingServiceImpl implements CourseOfferingService{
    private final UserService userService;
    private final CourseService courseService;

    private final CourseOfferingRepository repository;

    private final ModelMapper mapper;


    private CourseOfferingResponseDTO toResponseDTO(CourseOffering offering){
        // 데이터가 없는 경우를 대비합니다.
        String professorName = "공백";
        User professor = offering.getProfessor();
        if (professor != null && professor.getNickname() != null) {
            professorName = professor.getNickname();
        }

        String courseName = "공백";
        Course course = offering.getCourse();
        if(offering.getCourseName() == null) {
            if (course != null && course.getCourseName() != null) {
                courseName = course.getCourseName();
            }
        } else {
            courseName = offering.getCourseName();
        }

        return
                CourseOfferingResponseDTO.builder()
                        .offeringId(offering.getOfferingId())
                        .professorId(professor != null ? professor.getUserId() : null)
                        .professorName(professorName)
                        .courseName(courseName)
                        .year(offering.getYear())
                        .semester(offering.getSemester())
                        .courseId(course != null ? course.getCourseId() : null)
                        .build();
    }

    @Override
    public Optional<List<CourseOfferingResponseDTO>> findAllCourseOfferingDTO() {
        List<CourseOffering> courseOfferings = repository.findAll();
        List<CourseOfferingResponseDTO> legacyCourseOfferingDTOS = courseOfferings.stream().map(courseOffering ->
                mapper.map(courseOffering, CourseOfferingResponseDTO.class)).toList();

        Optional<List<CourseOfferingResponseDTO>> optionalCourseOfferingDTOS = Optional.of(legacyCourseOfferingDTOS);
        return optionalCourseOfferingDTOS;
    }

    @Override
    public List<CourseOfferingResponseDTO> findAllOffering() {
        log.info("2) Offering 전체조회 시작");
        List<CourseOffering> offerings = repository.findAll();

        log.info("3) Department 전체조회 성공");


        return offerings.stream()
                .map(this::toResponseDTO).toList();
    }

    @Override
    public List<CourseOfferingResponseDTO> findById(Long offeringId) {
        log.info("2) Offering 한개조회 시작 , offeringId : {}",offeringId);
        Optional<CourseOffering> offeringOptinal = repository.findById(offeringId);

        if(offeringOptinal.isEmpty()){
            throw new RuntimeException("Offering not found with id : " + offeringId);
        }

        CourseOfferingResponseDTO responseDTO = toResponseDTO(offeringOptinal.get());
        return List.of(responseDTO);
    }

    @Override
    public CourseOfferingResponseDTO createOfferingByAuthorizedUser(CourseOfferingCreateDTO dto, String email) {
        log.info("2)Offering 추가 시작 Offering : {}", dto);

        // CourseId와 CourseName은 다르므로 Id를통해 결과를 탐색합니다.
        User user = userService.getUserById(dto.getProfessorId());
        Course course = courseService.getByCourseId(dto.getCourseId());

        log.info("3-1)유저 탐색 : {}", user);
        log.info("3-2)코스 탐색 : {}", course);

        CourseOffering offering = new CourseOffering();
        offering.setProfessor(user);
        offering.setCourse(course);

        MapperUtil.updateFrom(dto, offering, new ArrayList<>());

        log.info("4)OfferingCreateDTO -> Offering : {}", offering);
        CourseOffering result = repository.save(offering);

        return toResponseDTO(result);
    }

    @Override
    public CourseOfferingResponseDTO updateOfferingByAuthorizedUser(CourseOfferingUpdateDTO dto, String email) {
        log.info("2)Offering 수정 시작 Offering : {}", dto);

        Optional<CourseOffering> offeringOptional = repository.findById(dto.getOfferingId());

        if(offeringOptional.isEmpty()) {
            throw new RuntimeException("Offering not found with id " + dto.getOfferingId());
        }

        CourseOffering offering = offeringOptional.get();

        // UserId와 UserName은 다르므로 Id를통해 결과를 탐색합니다.
        User user = userService.getUserById(dto.getProfessorId());
        log.info("3-2)유저 탐색 : {}", user);

        log.info("3) 수정 이전 Offering : {}", offering);
        MapperUtil.updateFrom(dto,offering,List.of("offeringId"));
        offering.setProfessor(user);

        log.info("5) 기존 Offering : {}",offering);
        CourseOffering updateOffering = repository.save(offering);

        log.info("6) Offering 수정 성공 updateOffering : {}", updateOffering);
        return toResponseDTO(updateOffering);
    }

    @Override
    public Map<String, String> deleteByOfferingId(Long offeringId, String email) {
        log.info("2) Offering 한개삭제 시작 , offeringId : {}", offeringId);

        Optional<CourseOffering> offeringOptional = repository.findById(offeringId);

        if(offeringOptional.isEmpty()) {
            return Map.of("Result","Failure");
        }

        repository.delete(offeringOptional.get());

        return Map.of("Result","Success");
    }


    @Override
    public int addCourseOffering(CourseOfferingResponseDTO legacyCourseOfferingDTO) {
        log.info("1) 확인 : {}", legacyCourseOfferingDTO);
        CourseOffering courseOffering = mapper.map(legacyCourseOfferingDTO,CourseOffering.class);
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
