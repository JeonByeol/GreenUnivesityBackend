package com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.service;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.entity.AcademicTerm;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.respository.AcademicTermRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
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
public class AcademicTermServiceImpl implements AcademicTermService {
    private final AcademicTermRepository repository;
    private final EntityMapper entityMapper;
    private final ModelMapper mapper;

    @Override
    public List<AcademicTermResponseDTO> findAllTerm() {
        log.info("1) Term 전체 조회 시작");
        List<AcademicTermResponseDTO> dtoList = new ArrayList<>();
        for(AcademicTerm academicTerm : repository.findAll()){
            AcademicTermResponseDTO dto = mapper.map(academicTerm, AcademicTermResponseDTO.class);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public AcademicTermResponseDTO findById(Long termId) {
        log.info("1) Term 한 건 조회 시작");
        Optional<AcademicTerm> term = repository.findById(termId);

        log.info("2) Term 한 건 조회 에러 처리");
        if(term.isEmpty()){
            throw new RuntimeException("Course not found with id: " + termId);
        }

        log.info("3) Term 한 건 조회 에러 처리");
        AcademicTermResponseDTO responseDTO = mapper.map(term.get(), AcademicTermResponseDTO.class);

        return responseDTO;
    }

    @Override
    public AcademicTermResponseDTO createTermByAuthorizedUser(AcademicTermCreateDTO dto, String email) {
        log.info("1) Term 한 건 추가 시작 : {}", dto);

        log.info("2) Term 변환 시작");
        AcademicTerm academicTerm = new AcademicTerm();
        MapperUtil.updateFrom(dto, academicTerm, List.of("termId"));
        log.info("3) Term 변환 끝 : {}",academicTerm);

        log.info("4) Term 결과 반환");
        AcademicTerm term = repository.save(academicTerm);
        AcademicTermResponseDTO responseDTO = mapper.map(term, AcademicTermResponseDTO.class);

        return responseDTO;
    }

    @Override
    public AcademicTermResponseDTO updateTermByAuthorizedUser(AcademicTermUpdateDTO dto, String email) {
        log.info("1) Term 한 건 수정 시작 : {}", dto);

        log.info("2) Term 한 건 조회");
        Optional<AcademicTerm> termOptional = repository.findById(dto.getTermId());
        if(termOptional.isEmpty()){
            throw new RuntimeException("Term not found with id: " + dto.getTermId());
        }

        AcademicTerm term = termOptional.get();
        log.info("3) Term 한 건 수정 전 : {}", term);

        MapperUtil.updateFrom(dto, term,List.of("termId"));
        log.info("4) Term 한 건 수정 성공 : {}", term);

        AcademicTerm responseTerm = repository.save(term);

        return mapper.map(responseTerm, AcademicTermResponseDTO.class);
    }

    @Override
    public Map<String, String> deleteByTermId(Long termId, String email) {
        log.info("1) Term 한 건 삭제 시작 : {}", termId);
        Optional<AcademicTerm> termOptional = repository.findById(termId);

        log.info("2) Term 한 건 조회 및 체크 : {}", termId);
        if(termOptional.isEmpty()){
            return Map.of("Result","Failure");
        }

        log.info("3) Term 한 건 삭제");
        repository.delete(termOptional.get());

        return Map.of("Result","Success");
    }
}
