package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto;

import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryApproveType;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryType;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StudentStatusHistoryUpdateDTO {
    private Long statusHistoryId;
    private StudentStatusHistoryType changeType;
    private StudentStatusHistoryApproveType approveType;
    private LocalDate changeDate;
    private String reason;
    private Long userId;
}
