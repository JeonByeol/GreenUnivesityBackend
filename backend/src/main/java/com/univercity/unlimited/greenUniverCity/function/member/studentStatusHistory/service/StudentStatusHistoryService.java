package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.service;

import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto.StudentStatusHistoryUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistory;

import java.util.List;
import java.util.Map;

public interface StudentStatusHistoryService {
    // 기본 5가지 구성 요소
    List<StudentStatusHistoryResponseDTO> findAllHistory();
    StudentStatusHistoryResponseDTO findById(Long historyId);
    StudentStatusHistoryResponseDTO createHistoryByAuthorizedUser(StudentStatusHistoryCreateDTO dto, String email);
    StudentStatusHistoryResponseDTO updateHistoryByAuthorizedUser(StudentStatusHistoryUpdateDTO dto, String email);
    Map<String, String> deleteByHistoryId(Long historyId, String email);

    StudentStatusHistoryResponseDTO approveStatusHistory(StudentStatusHistoryUpdateDTO dto, String email);
    StudentStatusHistoryResponseDTO rejectStatusHistory(StudentStatusHistoryUpdateDTO dto, String email);    // 반려

    List<StudentStatusHistoryResponseDTO> getMyData(String email);

    // 추후 entity를 찾아야하는 경우 추가
//    StudentStatusHistory findEntityById(Long historyId);
}
