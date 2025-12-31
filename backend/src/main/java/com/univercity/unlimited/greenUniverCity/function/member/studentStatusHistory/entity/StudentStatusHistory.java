package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity;

import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_sshistory")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long statusHistoryId;

    @Column(name = "change_type")
    @Enumerated(EnumType.STRING)
    private StudentStatusHistoryType changeType;

    @Column(name = "approve_type")
    @Enumerated(EnumType.STRING)
    private StudentStatusHistoryApproveType approveType;

    @Column(name = "change_date")
    private LocalDate changeDate;

    @Column(name = "reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    // 일단 보류
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "term_id")
//    @ToString.Exclude
//    private Teram

}
