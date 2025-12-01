package com.univercity.unlimited.greenUniverCity.function.community.notice.entity;

import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString

@Table(
        name="tbl_notice",indexes = {
                //외래키user_id
        @Index(columnList ="user_id",name="idx_notice_user")
        }
)
public class Notice {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    @Column(name="notice_id")
    private Long noticeId; //공지 아이디

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; //유저

    @Column(name="title",nullable = false)
    private String title; //타이틀

    @Column(name="content",nullable = false)
    private String content; //콘텐츠

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt; //게시날짜

}
