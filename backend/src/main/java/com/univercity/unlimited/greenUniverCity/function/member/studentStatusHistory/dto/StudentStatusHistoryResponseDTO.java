package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto;

import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryApproveType;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class StudentStatusHistoryResponseDTO {
    private Long statusHistoryId;
    private String changeType;
    private String approveType;
    private LocalDate changeDate;
    private String reason;
    private Long userId;
}
