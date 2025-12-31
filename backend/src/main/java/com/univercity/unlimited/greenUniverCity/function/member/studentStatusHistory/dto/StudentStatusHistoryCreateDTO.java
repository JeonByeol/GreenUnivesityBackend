package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto;

import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryType;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StudentStatusHistoryCreateDTO {
    private String changeType;
    private LocalDate changeDate;
    private String reason;
    private Long userId;
}

