package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.service;

import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistory;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.repository.StudentStatusHistoryRepository;
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
//        return StudentStatusHistoryResponseDTO
        return null;
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
    public List<StudentStatusHistoryResponseDTO> findById(Long historyId) {
        log.info("2) StudentStatusHistory 한개조회 시작, historyId: {}",historyId);

        Optional<StudentStatusHistory> historyOptinal = repository.findById(historyId);
        if(historyOptinal.isEmpty()) {
            throw new RuntimeException("StudentStatusHistory not found with id " + historyId);
        }

        StudentStatusHistoryResponseDTO responseDTO = toResponseDTO(historyOptinal.get());
        return List.of(responseDTO);
    }

    @Override
    public StudentStatusHistoryResponseDTO createHistoryByAuthorizedUser(StudentStatusHistoryCreateDTO dto, String email) {
        log.info("2)StudentStatusHistory 추가 시작 DTO : {}", dto);

        StudentStatusHistory history = 
    }

    @Override
    public StudentStatusHistoryResponseDTO updateHistoryByAuthorizedUser(StudentStatusHistoryCreateDTO dto, String email) {
        return null;
    }

    @Override
    public Map<String, String> deleteByHistoryId(Long historyId, String email) {
        return Map.of();
    }
}
