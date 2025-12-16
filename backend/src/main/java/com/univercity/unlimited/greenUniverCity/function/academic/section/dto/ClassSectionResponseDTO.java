package com.univercity.unlimited.greenUniverCity.function.academic.section.dto;

import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.SectionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSectionResponseDTO {
    //Section 정보
    private Long sectionId; // 분반 ID
    private String sectionName; // 분반명 (예: A반, 주간반)
    private Integer maxCapacity; // 정원 (예: 40)
    private Integer currentCount; // 현재 수강 신청한 인원 수 (Enrollment 테이블 count 결과)
    
    //SectionType 정보
    private SectionType sectionType;
    private String sectionTypeDisplay; // "온라인", "오프라인", "혼합형"

    //Classroom 정보
    private Long classroomId;
    private String classroomLocation; // 강의실 위치
    
    //CourseOffering 정보
    private Long offeringId; //개설강의 id
    private String courseName; //강의명
    private Integer year; //개설년도
    private String semester; //개설학기
    private String professorName; //담당교수

    // ** 계산 필드 - > 나중에 사용할 지 안할지 모름 현재는 사용 안함 / 사용 안하면 삭제
    private Integer availableSeats; // 남은 자리 = maxCapacity - currentCount
    private Boolean isFull; // 마감 여부 = currentCount >= maxCapacity
    private Double enrollmentRate; // 수강률 = (currentCount / maxCapacity) * 100

    // 편의 메서드- > 나중에 사용할 지 안할지 모름 현재는 사용 안함 / 사용 안하면 삭제
//    public Integer getAvailableSeats() {
//        return maxCapacity - currentCount;
//    }
//
//    public Boolean getIsFull() {
//        return currentCount >= maxCapacity;
//    }
//
//    public Double getEnrollmentRate() {
//        return maxCapacity > 0 ? (currentCount * 100.0 / maxCapacity) : 0.0;
//    }
}
