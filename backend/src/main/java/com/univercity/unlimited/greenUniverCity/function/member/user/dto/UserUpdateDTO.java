package com.univercity.unlimited.greenUniverCity.function.member.user.dto;

import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryApproveType;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryType;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserUpdateDTO {

    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String role;
    private String studentNumber;
    private String deptName;
    private boolean isDelete;
    private StudentStatusHistoryType currentStatus;
    private StudentStatusHistoryApproveType currentApprove;
    // 조건 예시
//    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다")
//    private String nickname;
}
