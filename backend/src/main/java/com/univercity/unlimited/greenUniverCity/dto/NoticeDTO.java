package com.univercity.unlimited.greenUniverCity.dto;

import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class NoticeDTO {
    private Integer notice_id;
    private UserVo userVo;
    private String title;
    private String content;
    private LocalDateTime created_at;
}
