package com.univercity.unlimited.greenUniverCity.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class UserDTO {
    private Long userId;

    private String email;
    private String password;
    private String nickname;
}
