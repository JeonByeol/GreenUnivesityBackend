package com.univercity.unlimited.greenUniverCity.function.notice.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserDTO;
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

    private LocalDateTime createdAt; //날짜




}
