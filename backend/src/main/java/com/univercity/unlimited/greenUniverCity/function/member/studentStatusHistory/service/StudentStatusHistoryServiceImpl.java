package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.service;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistory;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryApproveType;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryType;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.repository.StudentStatusHistoryRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.util.MapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentStatusHistoryServiceImpl implements StudentStatusHistoryService{
    private final StudentStatusHistoryRepository repository;

    private final UserService userService;

    private final ModelMapper mapper;

    private StudentStatusHistoryResponseDTO toResponseDTO(StudentStatusHistory history) {
        User user = history.getUser();


        String changeTypeStr;
        StudentStatusHistoryType changeTypeEnum = history.getChangeType();


        if (changeTypeEnum == StudentStatusHistoryType.LEAVE) { // 휴학
            changeTypeStr = "휴학";
        } else if (changeTypeEnum == StudentStatusHistoryType.EXPELLED) { // 제적
            changeTypeStr = "제적";
        } else if (changeTypeEnum == StudentStatusHistoryType.ENROLLED) { // 재학
            changeTypeStr = "재학";
        } else if (changeTypeEnum == StudentStatusHistoryType.GRADUATED) { // 졸업
            changeTypeStr = "졸업";
        } else {
            changeTypeStr = "기타";
        }

        String approveTypeStr;
        StudentStatusHistoryApproveType approveTypeEnum = history.getApproveType();

        if (approveTypeEnum == null || approveTypeEnum == StudentStatusHistoryApproveType.REQUESTED) { // 신청됨(미확정)
            approveTypeStr = "대기";
        } else if (approveTypeEnum == StudentStatusHistoryApproveType.APPROVED) { // 승인
            approveTypeStr = "승인";
        } else if (approveTypeEnum == StudentStatusHistoryApproveType.REJECTED) { // 거절
            approveTypeStr = "반려";
        } else {
            approveTypeStr = "기타";
        }

        Long userId = user != null ? user.getUserId() : null;

        return StudentStatusHistoryResponseDTO.builder()
                .statusHistoryId(history.getStatusHistoryId())
                .changeType(changeTypeStr)
                .approveType(approveTypeStr)
                .changeDate(history.getChangeDate())
                .reason(history.getReason())
                .userId(userId) // null 가능
                .build();
    }


    @Override
    public List<StudentStatusHistoryResponseDTO> findAllHistory() {
        List<StudentStatusHistoryResponseDTO> dtoList = new ArrayList<>();
        for(StudentStatusHistory i : repository.findAll()) {
            StudentStatusHistoryResponseDTO dto = toResponseDTO(i);
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
    public StudentStatusHistoryResponseDTO createHistoryByAuthorizedUser(
            StudentStatusHistoryCreateDTO dto,
            String email
    ) {
        log.info("2) StudentStatusHistory 추가 시작 DTO : {}", dto);

        User user = userService.getUserById(dto.getUserId());
        if (user == null) {
            throw new RuntimeException("유저를 찾을 수 없습니다: " + dto.getUserId());
        }

        StudentStatusHistory history = new StudentStatusHistory();

        // ===== changeType String -> Enum 변환 =====
        if ("LEAVE".equals(dto.getChangeType())) {
            history.setChangeType(StudentStatusHistoryType.LEAVE);
        } else if ("RETURN".equals(dto.getChangeType())) {
            // 복학을 하면, 재학상태가 되기 때문입니다.
            history.setChangeType(StudentStatusHistoryType.ENROLLED);
        }
//        else if ("ENROLLED".equals(dto.getChangeType())) {
//            history.setChangeType(StudentStatusHistoryType.ENROLLED);
//        }
        else if ("GRADUATED".equals(dto.getChangeType())) {
            history.setChangeType(StudentStatusHistoryType.GRADUATED);
        } else if ("EXPELLED".equals(dto.getChangeType())) {
            history.setChangeType(StudentStatusHistoryType.EXPELLED);
        } else {
            throw new IllegalArgumentException("알 수 없는 changeType: " + dto.getChangeType());
        }

        // ===== 기타 필드 setter =====
        history.setChangeDate(dto.getChangeDate());
        history.setReason(dto.getReason());
        history.setUser(user);
        history.setChangeDate(LocalDate.now());

        // 최초 생성은 무조건 대기 상태
        history.setApproveType(StudentStatusHistoryApproveType.REQUESTED);

        log.info("4) HistoryCreateDTO -> History : {}", history);

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

    @Override
    public StudentStatusHistoryResponseDTO approveStatusHistory(StudentStatusHistoryUpdateDTO dto, String email) {
        log.info("1) StudentStatusHistory 승인 시작, statusHistoryDTO: {}", dto);
        return processApproval(dto.getStatusHistoryId(), dto.getChangeType(),StudentStatusHistoryApproveType.APPROVED, email);
    }

    @Override
    public StudentStatusHistoryResponseDTO rejectStatusHistory(StudentStatusHistoryUpdateDTO dto, String email) {
        log.info("1) StudentStatusHistory 거절 시작, statusHistoryDTO: {}", dto);
        return processApproval(dto.getStatusHistoryId(), dto.getChangeType(), StudentStatusHistoryApproveType.REJECTED, email);
    }

    @Override
    public List<StudentStatusHistoryResponseDTO> getMyData(String studentEmail) {

        log.info("Service) 본인 학적 변경 이력 조회 시작 - email: {}", studentEmail);

        List<StudentStatusHistory> list =
                repository.findByUserEmail(studentEmail);

        if (list == null || list.isEmpty()) {
            log.info("Service) 조회 결과 없음 - email: {}", studentEmail);
            return List.of(); // 빈 리스트 반환
        }

        log.info("Service) 조회 완료 - email: {}, count: {}", studentEmail, list.size());
        return list.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // StudentStatusHistoryApproveType에 대한 처리 진행
    private StudentStatusHistoryResponseDTO processApproval(Long statusHistoryId,
                                                            StudentStatusHistoryType changeType,
                                                            StudentStatusHistoryApproveType approveType,
                                                            String email) {
        log.info("2) StudentStatusHistory 처리 시작, statusHistoryId: {}, approveType: {}", statusHistoryId, approveType);

        StudentStatusHistory history = repository.findById(statusHistoryId)
                .orElseThrow(() -> new RuntimeException("해당하는 statusHistoryId가 없습니다."));

        log.info("3) DB에서 가져온 History: {}", history);

        User user = history.getUser();
        log.info("4) 연관 User 정보: {}", user);

        // 히스토리에는 승인/거절 기록
        history.setApproveType(approveType);

        // User 업데이트
        UserUpdateDTO userUpdateDTO = userService.toEntity(user);

        if(approveType == StudentStatusHistoryApproveType.APPROVED) {
            userUpdateDTO.setCurrentStatus(history.getChangeType()); // 반드시 호출
        }

        userUpdateDTO.setCurrentApprove(StudentStatusHistoryApproveType.None);
        log.info("5) 변환된 UserUpdateDTO 정보: {}", userUpdateDTO);
        userService.updateUserByAuthorizedUser(userUpdateDTO, email);

        // 히스토리 저장
        StudentStatusHistory saved = repository.save(history);
        log.info("6) 저장된 History: {}", saved);

        return toResponseDTO(saved);
    }


}
