package com.univercity.unlimited.greenUniverCity.function.community.notice.dto;


import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class NoticeResponseDTO {
    private Long noticeId; //공지아이디

    private String title; //타이틀

    private String content; //콘텐츠

    private LocalDateTime createdAt; //날짜

    private Long userId;
    private String nickname; // 작성자 표시용



}