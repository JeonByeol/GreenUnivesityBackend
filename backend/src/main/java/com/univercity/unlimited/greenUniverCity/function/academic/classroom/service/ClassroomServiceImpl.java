package com.univercity.unlimited.greenUniverCity.function.academic.classroom.service;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.repository.ClassroomRepository;
import com.univercity.unlimited.greenUniverCity.function.community.review.exception.TimeTableNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassroomServiceImpl implements ClassroomService{

    private final ClassroomRepository repository;

    /**
     * CR-A) Classroom 엔티티를 (Response)DTO로 변환
     * - 각각의 crud 기능에 사용되는 서비스 구현부에 사용하기 위해 함수로 생성
     */
    private ClassroomResponseDTO toResponseDTO(Classroom classroom){
        return 
                ClassroomResponseDTO.builder()
                        .classroomId(classroom.getClassroomId())
                        .capacity(classroom.getCapacity())
                        .location(classroom.getLocation())
                        .build();
        
    }

    //CR-1) 강의실 테이블에 존재하는 모든 데이터를 조회하기 위한 서비스 구현부
    @Override
    public List<ClassroomResponseDTO> findAllClassroom() {
        log.info("2) 강의실 전체조회 시작");
        List<Classroom> classrooms=repository.findAll();

        log.info("3) 강의실 전체조회 성공");

        return classrooms.stream()
                .map(this::toResponseDTO)
                .toList();
    }
    
    //CR-2) 강의실 테이블에 존재하는 특정 강의실을 조회하기 위한 서비스 구현부
    @Override
    public List<ClassroomResponseDTO> findByLocation(String keyword) {
        log.info("2) 특정 키워드에 해당하는 강의실 조회 시작 keyword-:{}",keyword);
        List<Classroom> classrooms=repository.findByLocationContain(keyword);

        log.info("3) 특정 키워드에 해당하는 강의실 조회 성공 keyword-:{}",keyword);

        return classrooms.stream()
                .map(this::toResponseDTO)
                .toList();
    }
    
    //CR-3) 새로운 강의실을 생성하기 위한 서비스 구현부 | //보안검사 추가해야함
    @Override
    public ClassroomResponseDTO createClassroom(ClassroomCreateDTO dto, String email) {
        log.info("2) 강의실 생성 교수-:{}",email);
        
        Classroom classroom = Classroom.builder()
                .location(dto.getLocation()) // 예: "공학관 301호"
                .capacity(dto.getCapacity()) // 예: 50명
                .build();

        
        Classroom savedClassroom = repository.save(classroom);

        log.info("5)강의실 생성 완료 교수-:{}",email);

        return toResponseDTO(savedClassroom);
    }
    
    //CR-4) 기존에 존재하는 강의실의 정보를 수정하기 위한 서비스 구현부
    @Override
    public ClassroomResponseDTO updateClassroom(ClassroomUpdateDTO dto, String email) {
        log.info("2) 강의실 정보 수정 시작 classroomId-:{}, 교수-:{}",dto.getClassroomId(),email);

        Classroom classroom=repository.findById(dto.getClassroomId())
                .orElseThrow(()->new TimeTableNotFoundException(
                        "3)보안 검사 시도 식별 코드 -:CR-4 " +
                                "강의실 정보가 존재하지 않습니다 classroomId " + dto.getClassroomId()));

        classroom.setCapacity(dto.getCapacity());
        classroom.setLocation(dto.getLocation());

        Classroom updateClassroom=repository.save(classroom);

        log.info("5) 강의실 정보 수정 성공 교수-:{}, classroomId-{}, 정원-:{}, 장소-:{}",
                email,dto.getClassroomId(),dto.getCapacity(),dto.getLocation());

        return toResponseDTO(updateClassroom);
    }

    @Override
    public void deleteByClassroom(Long classroomId, String email) {
        log.info("2) 강의실 정보 삭제 요청 교수-:{}, classroomId-:{}",email,classroomId);

        Classroom classroom=repository.findById(classroomId)
                .orElseThrow(()->new TimeTableNotFoundException(
                        "3)보안 검사 시도 식별 코드 -:CR-4 " +
                                "강의실 정보가 존재하지 않습니다 classroomId " + classroomId));

        repository.delete(classroom);

        log.info("5) 강의실 정보 삭제 성공 교수-:{}, classroomId-:{}",email,classroomId);
    }
}
