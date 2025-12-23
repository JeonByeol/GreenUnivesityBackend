package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.dto;

import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentStatusHistoryCreateDTO {
    private StudentStatusHistoryType changeType;
    private String reason;
    private Long userId;
}

