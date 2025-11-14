package com.univercity.unlimited.greenUniverCity.function.notice.entity;

import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
    private Integer notice_id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="title",nullable = false)
    private String title;

    @Column(name="content",nullable = false)
    private String content;

    @Column(name="created_at",nullable = false)
    private LocalDateTime created_at;
}
