package com.univercity.unlimited.greenUniverCity.function.academic.classroom.service;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.repository.ClassroomRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClassroomServiceImpl implements ClassroomService{

    private final ClassroomRepository repository;

    private final EntityMapper entityMapper;

    private final AcademicSecurityValidator validator;

    //CR-1) 강의실 테이블에 존재하는 모든 데이터를 조회하기 위한 서비스 구현부
    @Override
    @Transactional(readOnly = true)
    public List<ClassroomResponseDTO> findAllClassroom() {
        log.info("2) 강의실 전체조회 시작");

        return repository.findAll().stream()
                .map(entityMapper::toClassroomResponseDTO)
                .toList();
    }
    
    //CR-2) 강의실 테이블에 존재하는 특정 강의실을 조회하기 위한 서비스 구현부
    @Override
    @Transactional(readOnly = true)
    public List<ClassroomResponseDTO> findByLocation(String keyword) {
        log.info("2) 특정 키워드에 해당하는 강의실 조회 시작 keyword-:{}",keyword);

        return repository.findByLocationContain(keyword).stream()
                .map(entityMapper::toClassroomResponseDTO)
                .toList();
    }

    //CR-2-1) 강의실 단건 조회
    @Override
    @Transactional(readOnly = true)
    public ClassroomResponseDTO getRoom(Long classroomId) {
        log.info("2) 강의실 단건 조회 시작 - classroomId-:{}", classroomId);

        Classroom classroom= validator.getEntityOrThrow(repository,classroomId,"강의실");

        return entityMapper.toClassroomResponseDTO(classroom);
    }

    //CR-3) 새로운 강의실을 생성하기 위한 서비스 구현부 | //보안검사 추가해야함
    @Override
    public ClassroomResponseDTO createClassroom(ClassroomCreateDTO dto, String email) {
        log.info("2) 강의실 생성 시작 - 관리자-:{}, 위치-:{}", email, dto.getLocation());
        
        validator.validateAdminRole(email,"강의실 생성");

        boolean exists = repository.existsByLocation(dto.getLocation());
        validator.validateDuplicate(exists, "강의실(" + dto.getLocation() + ")");
        
        Classroom classroom = Classroom.builder()
                .location(dto.getLocation())
                .capacity(dto.getCapacity())
                .build();

        return entityMapper.toClassroomResponseDTO(repository.save(classroom));
    }
    
    //CR-4) 기존에 존재하는 강의실의 정보를 수정하기 위한 서비스 구현부
    @Override
    public ClassroomResponseDTO updateClassroom(ClassroomUpdateDTO dto, String email) {
        Long classroomId = dto.getClassroomId();
        String location = dto.getLocation();
        log.info("2) 강의실 정보 수정 시작 classroomId-:{}, 관리자-:{}", classroomId, email);

        Classroom classroom = validator.getEntityOrThrow(repository, classroomId, "강의실");

        validator.validateAdminRole(email,"강의실 수정");

        // 3. [검증] 위치(이름)가 변경되는 경우에만 중복 검사 수행
        if (!classroom.getLocation().equals(location)) {
            boolean exists = repository.existsByLocation(location);
            validator.validateDuplicate(exists, "강의실(" + location + ")");
        }

        classroom.updateClassroomInfo(location, dto.getCapacity());

        log.info("5) 강의실 수정 완료");
        return entityMapper.toClassroomResponseDTO(repository.save(classroom));
    }
    
    //CR-5) 존재하는 강의실을 삭제하기 위한 서비스 구현부
    @Override
    @Transactional
    public void deleteByClassroom(Long classroomId, String email) {
        log.info("2) 강의실 정보 삭제 시작 - 관리자-:{}, classroomId-:{}", email, classroomId);

        Classroom classroom = validator.getEntityOrThrow(repository, classroomId, "강의실");

        validator.validateAdminRole(email, "강의실 삭제");
        
        repository.delete(classroom);
        log.info("5) 강의실 삭제 완료");
    }

    //CR-6) 외부 service에서 classroom의 정보를 조회하기 위한 service선언부
    @Override
    public Classroom getClassroomEntity(Long classroomId) {
        return validator.getEntityOrThrow(repository, classroomId, "강의실");
    }
}
