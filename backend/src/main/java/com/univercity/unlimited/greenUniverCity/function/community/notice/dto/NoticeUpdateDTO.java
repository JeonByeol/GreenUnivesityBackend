package com.univercity.unlimited.greenUniverCity.function.community.notice.dto;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class NoticeUpdateDTO {
    private Long noticeId; //공지아이디

    private String title; //타이틀

    private String content; //콘텐츠

    private LocalDateTime createdAt; // 개시 날짜

    private LocalDateTime updatedAt; //수정날짜




}
