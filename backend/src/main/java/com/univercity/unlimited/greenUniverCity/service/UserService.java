package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;

import java.util.List;

public interface UserService {
//    모든 사용자 조회
    List<UserVo> findAllUsers();

//    특정 역할을 가진 모든 사용자를 조회합니다.
    List<UserVo> findUsersByRole(UserRole role);

    UserDTO login(UserDTO userDTO);
    
}
