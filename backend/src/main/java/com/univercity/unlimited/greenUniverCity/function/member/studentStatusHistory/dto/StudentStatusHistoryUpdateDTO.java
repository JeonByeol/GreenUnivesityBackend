package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto;

import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryType;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentStatusHistoryUpdateDTO {
    private Long statusHistoryId;
    private StudentStatusHistoryType changeType;
    private LocalDate changeDate;
    private String reason;
    private Long userId;
}
