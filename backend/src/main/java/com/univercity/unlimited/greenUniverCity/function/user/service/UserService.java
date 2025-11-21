package com.univercity.unlimited.greenUniverCity.function.user.service;

import com.univercity.unlimited.greenUniverCity.function.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.user.entity.UserRole;

import java.util.List;

public interface UserService {
    //전체 유저 데이터를 조회하기 위한 service
    List<UserDTO> findAllUsers();

//    //하나의 유저 데이터 조회하기 위한 service
//    User findByUser(String id);

    //특정 역활에 속한 모든 유저를 조회하기 위한 service
    List<User> findUsersByRole(UserRole role);

    //user정보를 통하여 로그인을 시키기 위한 service
    UserDTO login(UserDTO userDTO);

    //user정보가 없는 사람을 회원가입을 통해 정보를 만들기 위한 service
    UserDTO register(UserDTO dto);

    //
    User getUserById(Long userId);

    //교수 권한이 있는 사용자만 조회
    User getProfessorById(Long userId);
}
