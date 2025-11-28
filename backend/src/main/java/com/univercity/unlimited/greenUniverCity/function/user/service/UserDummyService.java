package com.univercity.unlimited.greenUniverCity.function.user.service;

import com.univercity.unlimited.greenUniverCity.function.user.dto.UserLoginDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserRegisterDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.user.entity.UserDummy;
import com.univercity.unlimited.greenUniverCity.function.user.entity.UserRole;
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
