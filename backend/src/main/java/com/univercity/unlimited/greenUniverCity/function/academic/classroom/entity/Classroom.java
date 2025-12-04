package com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Table(name = "classroom") // DB 테이블명 명시
@Builder
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    private Long classroomId; //강의실 Id
    
    @Column(name = "location", nullable = false)
    private String location; //장소(A건물,B건물)

    @Column(name = "capacity", nullable = false)
    private Integer capacity; //수용인원

    // --- 비즈니스 로직 (수정 편의 메서드) ---
    public void updateClassroomInfo(String location, Integer capacity) {
        if (location != null) this.location = location;
        if (capacity != null) this.capacity = capacity;
    }
}