package com.univercity.unlimited.greenUniverCity.function.academic.section.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "tbl_class_section")
@ToString
public class ClassSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long sectionId; // 분반 ID

    @Column(name = "section_name", nullable = false, length = 20)
    private String sectionName; // 분반명 (예: A반, 주간반)

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity; // 정원 (예: 40)

    @Column(name = "section_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SectionType sectionType; //사용가능,사용불가 ...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id", nullable = false)
    @ToString.Exclude // Lombok 무한 루프 방지
    private CourseOffering courseOffering;

    // 추가: 이 분반의 시간표들 (1:N)
    // cascade = ALL: 분반 삭제 시 시간표도 함께 삭제
    // orphanRemoval = true: 시간표가 리스트에서 제거되면 DB에서도 삭제
    @Formula("(SELECT COUNT(e.enrollment_id) FROM tbl_enrollment e WHERE e.section_id = section_id)")
    private Integer currentCount;

    @OneToMany(mappedBy = "classSection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    @Builder.Default
    private List<TimeTable> timeTables = new ArrayList<>();

    // 편의 메서드
    public void addTimeTable(TimeTable timeTable) {
        timeTables.add(timeTable);
        timeTable.setClassSection(this);
    }

    public void removeTimeTable(TimeTable timeTable) {
        timeTables.remove(timeTable);
        timeTable.setClassSection(null);
    }

    public void updateSectionInfo(String sectionName, Integer maxCapacity, SectionType sectionType) {
        if (sectionName != null) this.sectionName = sectionName;
        if (maxCapacity != null) this.maxCapacity = maxCapacity;
        if (sectionType != null) this.sectionType = sectionType;
    }
}
