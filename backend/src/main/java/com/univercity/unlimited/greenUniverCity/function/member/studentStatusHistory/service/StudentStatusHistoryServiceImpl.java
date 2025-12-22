package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.service;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistory;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.repository.StudentStatusHistoryRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.util.MapperUtil;
import jakarta.transaction.Transactional;
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
public class StudentStatusHistoryServiceImpl implements StudentStatusHistoryService{
    private final StudentStatusHistoryRepository repository;
    private final ModelMapper mapper;

    private StudentStatusHistoryResponseDTO toResponseDTO(StudentStatusHistory history) {
        User user = history.getUser();

        return StudentStatusHistoryResponseDTO.builder()
                .statusHistoryId(history.getHistoryId())
                .changeType(history.getChangeType())
                .reason(history.getReason())
                .userId(user != null ? user.getUserId() : 1l)
                .build();
    }

    @Override
    public List<StudentStatusHistoryResponseDTO> findAllHistory() {
        List<StudentStatusHistoryResponseDTO> dtoList = new ArrayList<>();
        for(StudentStatusHistory i : repository.findAll()) {
            StudentStatusHistoryResponseDTO dto = mapper.map(i, StudentStatusHistoryResponseDTO.class);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public StudentStatusHistoryResponseDTO findById(Long historyId) {
        log.info("2) StudentStatusHistory 한개조회 시작, historyId: {}",historyId);

        Optional<StudentStatusHistory> historyOptinal = repository.findById(historyId);
        if(historyOptinal.isEmpty()) {
            throw new RuntimeException("StudentStatusHistory not found with id " + historyId);
        }

        StudentStatusHistoryResponseDTO responseDTO = toResponseDTO(historyOptinal.get());
        return responseDTO;
    }

    @Override
    public StudentStatusHistoryResponseDTO createHistoryByAuthorizedUser(StudentStatusHistoryCreateDTO dto, String email) {
        log.info("2)StudentStatusHistory 추가 시작 DTO : {}", dto);

        StudentStatusHistory history = new StudentStatusHistory();
        MapperUtil.updateFrom(dto, history, new ArrayList<>());

        log.info("4)HistoryCreateDTO -> History : {}", history);
        StudentStatusHistory result = repository.save(history);

        return toResponseDTO(result);
    }

    @Override
    public StudentStatusHistoryResponseDTO updateHistoryByAuthorizedUser(StudentStatusHistoryUpdateDTO dto, String email) {
        log.info("2)History 수정 시작 Offering : {}", dto);

        Optional<StudentStatusHistory> historyOptional = repository.findById(dto.getStatusHistoryId());

        if(historyOptional.isEmpty()){
            throw new RuntimeException("History not found with id " + dto.getStatusHistoryId());
        }

        StudentStatusHistory history = historyOptional.get();

        log.info("3) 수정 이전 History : {}", history);
        MapperUtil.updateFrom(dto,history,List.of("historyId"));

        log.info("5) 기존 History : {}",history);
        StudentStatusHistory updateHistory = repository.save(history);

        log.info("5) 수정 이후 History : {}",updateHistory);
        return toResponseDTO(updateHistory);
    }

    @Override
    public Map<String, String> deleteByHistoryId(Long historyId, String email) {
        log.info("2) History 한개삭제 시작 , historyId : {}", historyId);

        Optional<StudentStatusHistory> historyOptional = repository.findById(historyId);

        if(historyOptional.isEmpty()) {
            return Map.of("Result","Failure");
        }

        repository.delete(historyOptional.get());

        return Map.of("Result","Success");
    }
}
