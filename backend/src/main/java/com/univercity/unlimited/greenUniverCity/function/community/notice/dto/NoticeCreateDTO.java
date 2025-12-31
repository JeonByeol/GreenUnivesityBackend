package com.univercity.unlimited.greenUniverCity.function.community.notice.dto;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class NoticeCreateDTO {

    private String title; //타이틀

    private String content; //콘텐츠

    private LocalDateTime createdAt; //날짜

    private String  nickname; //날짜


//    @JsonBackReference("user-notice")
//    @ToString.Exclude
//    private UserDTO userDTO;

}
