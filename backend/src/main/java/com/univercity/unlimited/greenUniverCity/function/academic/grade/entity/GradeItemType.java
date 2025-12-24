package com.univercity.unlimited.greenUniverCity.function.academic.grade.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


public enum GradeItemType {
    MIDTERM,    // 중간고사
    FINAL,      // 기말고사
    ASSIGNMENT, // 과제
    ATTENDANCE, // 출석
    ETC         // 기타
}