package com.univercity.unlimited.greenUniverCity.function.member.user.service;

import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserLoginDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserRegisterDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserDummy;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import jakarta.transaction.Transactional;

import java.util.List;

public interface UserDummyService {

    List<UserResponseDTO> findAllUsers();

    List<UserResponseDTO> findUsersByRole(UserRole role);

    UserResponseDTO login(UserLoginDTO loginDTO);

    @Transactional
    UserResponseDTO register(UserRegisterDTO registerDTO);

    UserDummy getUserById(Long userId);

    UserDummy getProfessorById(Long userId);

    UserDummy getUserByEmail(String email);

    @Transactional
    UserResponseDTO updateUser(Long userId, UserUpdateDTO updateDTO);
}
