package com.univercity.unlimited.greenUniverCity.function.academic.classroom.service;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.repository.ClassroomRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.InvalidRoleException;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ClassroomServiceImpl implements ClassroomService{

    private final ClassroomRepository repository;

    private final UserService userService;

    private final EntityMapper entityMapper;

    /**
     * CR-security) 보안검사:
     * - 권한 검증: 교수 or 관리자의 권한을 확인
     */
    private void validateOwnerShip(String requesterEmail,String action){
        log.info("4) 보안 검증 시작 - 강의실 {}  요청자-:   {},",action,requesterEmail);

        User requester= userService.getUserByEmail(requesterEmail);

        if(!(requester.getUserRole() == UserRole.PROFESSOR
                || requester.getUserRole() == UserRole.ADMIN)){
            throw new InvalidRoleException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: CR-security-1 (강의실   %s) " +
                                    "권한이 존재하지 않습니다 " +
                                    "요청자: %s, userId: %s, 현재역할: %s",
                            action,requesterEmail,requester.getUserId(),requester.getUserRole())
                    );

        }

        log.info("4) 보안 검증 완료 - 강의실-:{} 요청자-:{}, ",action,requesterEmail);

    }


    //CR-1) 강의실 테이블에 존재하는 모든 데이터를 조회하기 위한 서비스 구현부
    @Override
    public List<ClassroomResponseDTO> findAllClassroom() {
        log.info("2) 강의실 전체조회 시작");
        List<Classroom> classrooms=repository.findAll();

        log.info("3) 강의실 전체조회 성공");

        return classrooms.stream()
                .map(entityMapper::toClassroomResponseDTO)
                .toList();
    }
    
    //CR-2) 강의실 테이블에 존재하는 특정 강의실을 조회하기 위한 서비스 구현부
    @Override
    public List<ClassroomResponseDTO> findByLocation(String keyword) {
        log.info("2) 특정 키워드에 해당하는 강의실 조회 시작 keyword-:{}",keyword);
        List<Classroom> classrooms=repository.findByLocationContain(keyword);

        log.info("3) 특정 키워드에 해당하는 강의실 조회 성공 keyword-:{}",keyword);

        return classrooms.stream()
                .map(entityMapper::toClassroomResponseDTO)
                .toList();
    }

    //CR-2-1)
    @Override
    public ClassroomResponseDTO getRoom(Long classroomId) {
        log.info("2) 강의실 단건 조회 시작 - classroomId-:{}", classroomId);

        Classroom classroom= repository.findById(classroomId)
                .orElseThrow(()-> new IllegalArgumentException("강의실 정보를 찾을 수 없습니다."));

        return entityMapper.toClassroomResponseDTO(classroom);
    }

    //CR-3) 새로운 강의실을 생성하기 위한 서비스 구현부 | //보안검사 추가해야함
    @Override
    @Transactional
    public ClassroomResponseDTO createClassroom(ClassroomCreateDTO dto, String email) {
        log.info("2) 강의실 생성 교수-:{}",email);
        
        validateOwnerShip(email,"생성");
        
        Classroom classroom = Classroom.builder()
                .location(dto.getLocation())
                .capacity(dto.getCapacity())
                .build();

        Classroom savedClassroom = repository.save(classroom);

        log.info("5)강의실 생성 완료 교수-:{}",email);

        return entityMapper.toClassroomResponseDTO(savedClassroom);
    }
    
    //CR-4) 기존에 존재하는 강의실의 정보를 수정하기 위한 서비스 구현부
    @Override
    @Transactional
    public ClassroomResponseDTO updateClassroom(ClassroomUpdateDTO dto, String email) {
        log.info("2) 강의실 정보 수정 시작 classroomId-:{}, 교수-:{}",dto.getClassroomId(),email);

        Classroom classroom=repository.findById(dto.getClassroomId())
                .orElseThrow(()->new EntityNotFoundException(
                        "3)보안 검사 시도 식별 코드 -:CR-4 " +
                                "강의실 정보가 존재하지 않습니다 classroomId " + dto.getClassroomId()));

        validateOwnerShip(email,"수정");

        classroom.updateClassroomInfo(dto.getLocation(), dto.getCapacity());

        log.info("5) 강의실 정보 수정 성공 교수-:{}, classroomId-{}, 정원-:{}, 장소-:{}",
                email,dto.getClassroomId(),dto.getCapacity(),dto.getLocation());

        return entityMapper.toClassroomResponseDTO(classroom);
    }
    
    //CR-5) 존재하는 강의실을 삭제하기 위한 서비스 구현부
    @Override
    @Transactional
    public void deleteByClassroom(Long classroomId, String email) {
        log.info("2) 강의실 정보 삭제 요청 교수-:{}, classroomId-:{}",email,classroomId);

        Classroom classroom=repository.findById(classroomId)
                .orElseThrow(()->new EntityNotFoundException(
                        "3)보안 검사 시도 식별 코드 -:CR-5 " +
                                "강의실 정보가 존재하지 않습니다 classroomId " + classroomId));
        
        validateOwnerShip(email,"삭제");
        
        repository.delete(classroom);

        log.info("5) 강의실 정보 삭제 성공 교수-:{}, classroomId-:{}",email,classroomId);
    }

    //CR-6) 외부 service에서 classroom의 정보를 조회하기 위한 service선언부
    @Override
    public Classroom getClassroomEntity(Long classroomId) {
        return repository.findById(classroomId)
                .orElseThrow(()->new IllegalArgumentException("강의실 정보를 찾을 수 없습니다."));
    }
}
