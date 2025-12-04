package com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomResponseDTO{
    private Long classroomId; // 강의실 ID
    private String location; // 건물/호수 (예: 공학관 301호)
    private Integer capacity; // 수용 인원

    // Classroom은 다른 엔티티(ClassSection 등)에서 참조되는 입장이므로,
    // 현재로서는 추가적인 연관 정보(강의 목록 등) 없이 기본 정보만 반환하는 것이 깔끔합니다.

    // ** 편의 메서드 (필요시 사용, 예시) **
    // 강의실 크기 분류 (예: 소형, 중형, 대형)
    public String getSizeCategory() {
        if (capacity == null) return "Unknown";
        if (capacity >= 100) return "대형 강의실";
        if (capacity >= 50) return "중형 강의실";
        return "소형 강의실";
    }
}
