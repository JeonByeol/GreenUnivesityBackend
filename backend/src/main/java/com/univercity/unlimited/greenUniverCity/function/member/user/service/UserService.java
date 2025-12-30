package com.univercity.unlimited.greenUniverCity.function.member.user.service;

import com.univercity.unlimited.greenUniverCity.function.member.user.dto.*;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;

import java.util.List;
import java.util.Map;

public interface UserService {
    //전체 유저 데이터를 조회하기 위한 service
    List<UserResponseDTO> findAllUsers();

    //특정 역활에 속한 모든 유저를 조회하기 위한 service
    List<User> findUsersByRole(UserRole role);

    //user정보를 통하여 로그인을 시키기 위한 service
    UserResponseDTO login(UserLoginDTO userDTO);

    //user정보가 없는 사람을 회원가입을 통해 정보를 만들기 위한 service
    UserResponseDTO register(UserRegisterDTO dto);

    //U-U) Id를 통하여 학생 권한이 있는 사용자만 조회 및 검증을 하기 위한 service 구현부
    User getUserById(Long userId);

    //U-P) Id를 통하여 교수 권한이 있는 사용자만 조회 및 검증을 하기 위한 service 구현부
    User getProfessorById(Long userId);

    //U-E) Email을 통하여 사용자를 조회하기 위한 service 구현부
    User getUserByEmail(String email);

    //U-GP) NickName 정보를 통하여 user를 탐색하기 위한

    // U-CP) 이메일과 비밀번호를 입력받은 후에, 비밀번호를 변경
    UserResponseDTO changePassword(String password, String email);

    // U-D) 해당 유저 데이터 비활성화
    Map<String,String> deleteByUserEmail(Long userId,String email);

    // U-DR) 해당 유저 데이터 활성화
    Map<String,String> restoreByUserEmail(Long userId,String email);

    // U-U) 유저 정보 업데이트
    UserResponseDTO updateUserByAuthorizedUser(UserUpdateDTO dto, String email);
}
