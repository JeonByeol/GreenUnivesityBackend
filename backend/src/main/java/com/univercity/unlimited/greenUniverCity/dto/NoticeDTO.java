package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    private String title;

    private String content;

    private LocalDateTime created_at;

    @JsonBackReference("user-notice")
    @ToString.Exclude
    private UserDTO userDTO;


}
