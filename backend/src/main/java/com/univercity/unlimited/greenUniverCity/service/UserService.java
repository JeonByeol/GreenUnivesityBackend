package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;

import java.util.List;

public interface UserService {
    List<UserDTO> findAllUsers();
    //전체 유저 데이터를 조회하기 위한 service
    UserVo findByUser(String id);
    //하나의 유저 데이터 조회하기 위한 service
    List<UserVo> findUsersByRole(UserRole role);
    //특정 역활에 속한 모든 유저를 조회하기 위한 service
    UserDTO login(UserDTO userDTO);
    //user정보를 통하여 로그인을 시키기 위한 service
    UserDTO register(UserDTO dto);

    UserDTO grade(Long userId);
}
